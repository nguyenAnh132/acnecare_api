package com.acnecare.api.common.storage;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileStorageService fileStorageService;

    @PostMapping("/upload")
    ApiResponse<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder") StorageFolder folder
    ) {
        String userId = CurrentUserId.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return ApiResponse.<FileUploadResponse>builder()
                .code(1000)
                .message("File uploaded successfully")
                .result(fileStorageService.store(file, folder, userId))
                .build();
    }

    @DeleteMapping("/delete")
    ApiResponse<Void> deleteFile(@RequestParam("url") String fileUrl) {
        fileStorageService.delete(fileUrl);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("File deleted successfully")
                .build();
    }
}
