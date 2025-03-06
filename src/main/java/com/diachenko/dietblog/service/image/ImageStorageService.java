package com.diachenko.dietblog.service.image;
/*  diet-blog
    20.02.2025
    @author DiachenkoDanylo
*/

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface ImageStorageService {
    String saveImage(InputStream imageStream, String fileName) throws IOException;

    InputStream loadImage(String fileName) throws IOException;

    public String saveImagePart(Part filePart) throws IOException, NoSuchAlgorithmException;
}
