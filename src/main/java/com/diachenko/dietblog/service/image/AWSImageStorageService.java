package com.diachenko.dietblog.service.image;
/*  diet-blog
    20.02.2025
    @author DiachenkoDanylo
*/

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public class AWSImageStorageService implements ImageStorageService {
    private final S3Client s3;
    private final String bucketName = System.getenv("S3_BUCKET");
    private final String bucketRegion = System.getenv("AWS_REGION");

    public AWSImageStorageService() {
        this.s3 = S3Client.builder()
                .region(Region.of(bucketRegion))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    @Override
    public String saveImage(InputStream imageStream, String fileName) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl("public-read")  // Allow public access
                .build();

        s3.putObject(request, RequestBody.fromInputStream(imageStream, imageStream.available()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName; // Return S3 URL
    }

    @Override
    public InputStream loadImage(String fileName) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3.getObject(request);
    }

    @Override
    public String saveImagePart(Part filePart) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        InputStream imageStream = filePart.getInputStream();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl("public-read")  // Allow public access
                .build();

        s3.putObject(request, RequestBody.fromInputStream(imageStream, imageStream.available()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName; // Return S3 URL
    }
}
