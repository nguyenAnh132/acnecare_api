package com.acnecare.api.common.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService {
    private final StorageProperties storageProperties;
    
    public LocalFileStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }
  @Override
public FileUploadResponse store(MultipartFile file, StorageFolder folder, String userId) {
    if (file.isEmpty()) {
        throw new AppException(ErrorCode.FILE_EMPTY);
    }
    if (file.getSize() > storageProperties.getMaxFileSize()) {
        throw new AppException(ErrorCode.FILE_TOO_LARGE);
    }
    List<String> allowedTypes = Arrays.asList(
            storageProperties.getAllowedTypes().split(",")
    );
    if (!allowedTypes.contains(file.getContentType())) {
        throw new AppException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
    }

    try {
        Path userFolder = Paths.get(storageProperties.getUploadDir(), folder.getPath(), userId);
        Files.createDirectories(userFolder);

        long limitSize = folder == StorageFolder.facescan
                ? storageProperties.getMaxFacescanUserSize()
                : storageProperties.getMaxUserSize();

        long currentSize = calculateFolderSize(userFolder);
        if (currentSize + file.getSize() > limitSize) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID() + extension;

        Path filePath = userFolder.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        String fileUrl = storageProperties.getBaseUrl() + "/" + folder.getPath() + "/" + userId + "/" + newFilename;
        return FileUploadResponse.builder()
                .url(fileUrl)
                .filename(originalFilename)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();

    } catch (AppException e) {
        throw e;
    } catch (IOException e) {
        log.error("Failed to store file: {}", e.getMessage());
        throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
}


     @Override
    public void delete(String fileUrl) {
        try {
            // Cắt URL lấy phần path tương đối
            String relativePath = fileUrl.replace(storageProperties.getBaseUrl() + "/", "");
            Path filePath = Paths.get(storageProperties.getUploadDir(), relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    // Tính tổng dung lượng của 1 thư mục (đệ quy)
    private long calculateFolderSize(Path folder) throws IOException {
        if (!Files.exists(folder)) return 0;
        return Files.walk(folder)
                .filter(Files::isRegularFile)
                .mapToLong(path -> path.toFile().length())
                .sum();
    }
    
}
