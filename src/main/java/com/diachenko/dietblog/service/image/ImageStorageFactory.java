package com.diachenko.dietblog.service.image;
/*  diet-blog
    20.02.2025
    @author DiachenkoDanylo
*/

public class ImageStorageFactory {
    public static ImageStorageService getStorageService() {
        String storageType = System.getenv("STORAGE_TYPE");

        if ("S3".equalsIgnoreCase(storageType)) {
            return new AWSImageStorageService();
        } else {
            return new LocalImageStorageService();
        }
    }
}
