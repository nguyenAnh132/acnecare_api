package com.acnecare.api.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileUploadResponse store(MultipartFile file, StorageFolder  folder, String userId);
    void delete(String fileUrl);
}
// Sau này cần chuyển sang AWS S3 hoặc Google Cloud Storage thì chỉ cần implement lại.