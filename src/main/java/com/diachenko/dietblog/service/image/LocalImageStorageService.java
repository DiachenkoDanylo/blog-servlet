package com.diachenko.dietblog.service.image;
/*  diet-blog
    20.02.2025
    @author DiachenkoDanylo
*/

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LocalImageStorageService implements ImageStorageService {
    private static final String UPLOAD_DIR = "uploads/";

    public LocalImageStorageService() {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public String saveImage(InputStream imageStream, String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(imageStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        return UPLOAD_DIR + fileName;
    }

    @Override
    public String saveImagePart(Part filePart) throws IOException, NoSuchAlgorithmException {
        String fileName = filePart.getSubmittedFileName();
        InputStream imageStream = filePart.getInputStream();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Compute hash of the new image
        String newImageHash = computeImageHash(imageStream);

        int counter = 1;
        String newFileName = fileName;
        while (Files.exists(filePath)) {
            // Compute hash of the existing file
            String existingImageHash = computeImageHash(Files.newInputStream(filePath));

            // If hashes match, return the existing file path
            if (newImageHash.equals(existingImageHash)) {
                return filePath.toString();
            }

            // Otherwise, create a new file name with a counter
            String extension = "";
            String baseName = fileName;

            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = fileName.substring(dotIndex);
                baseName = fileName.substring(0, dotIndex);
            }

            newFileName = baseName + "_" + counter + extension;
            filePath = Paths.get(UPLOAD_DIR, newFileName);
            counter++;
        }

        // Save the file with the new name
        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @Override
    public InputStream loadImage(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        return new FileInputStream(filePath.toFile());
    }

    private String computeImageHash(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        inputStream.close();
        return bytesToHex(digest.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
