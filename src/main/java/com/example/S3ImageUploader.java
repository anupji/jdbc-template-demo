package com.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class S3ImageUploader {

    public static void main(String[] args) throws IOException {
        String bucketName = "imagebucket340459460742";
        String filePath = "image/window.jpg"; // Local image file
        String fileName = filePath.indexOf("/") != -1 ? filePath.substring(filePath.lastIndexOf("/") + 1) : filePath;
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        LocalDateTime currentDateTime = LocalDateTime.now();
        long unixTimestamp = currentDateTime.toEpochSecond(ZoneOffset.UTC);

        String prefix =  extension+"/";
        String fileNameModified = fileName + "_";
        String blobFileName = fileNameModified + String.valueOf(unixTimestamp) + "." +extension;
        fileName = prefix+blobFileName;
        System.out.println("Modified file name: " + fileName);
        String keyName = "folder-bucket-image/"+fileName; // S3 object key

        uploadImage(bucketName, keyName, filePath);
    }

    public static void uploadImage(String bucketName, String keyName, String filePath) throws IOException {
        Region region = Region.US_EAST_1; // Change to your region
        /*S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();*/
        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY")
                        )
                )
                .build();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType("image/jpeg") // or image/png
                .build();

        ClassLoader classLoader = S3ImageUploader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new RuntimeException("File not found in resources!");
        }

        long contentLength = Objects.requireNonNull(classLoader.getResource(filePath))
                .openConnection().getContentLengthLong();
        /*PutObjectResponse response = s3.putObject(
                request,
                RequestBody.fromFile(Paths.get(filePath))
        );*/
        PutObjectResponse response = s3.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        System.out.println("Image uploaded. ETag: " + response.eTag());
        s3.close();
    }
}
