package com.acnecare.api.common.storage;

import lombok.Getter;

@Getter
public enum StorageFolder {
    avatar("avatar", true),       // public
    license("license", false),    // private
    logo("logo", true),           // public
    products("products", true),   // public
    posts("posts", true),         // public
    facescan("facescan", false);  // private

    private final String path;
    private final boolean isPublic;

    StorageFolder(String path, boolean isPublic) {
        this.path = path;
        this.isPublic = isPublic;
    }

    public static StorageFolder fromPath(String path) {
        for (StorageFolder folder : values()) {
            if (folder.path.equalsIgnoreCase(path)) {
                return folder;
            }
        }
        throw new IllegalArgumentException("Invalid folder: " + path);
    }
}
