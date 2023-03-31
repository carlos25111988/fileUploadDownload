package com.aws.fileupdown.service.impl;

import com.amazonaws.services.codecommit.model.FileMetadata;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.fileupdown.config.files.impl.FileResource;
import com.aws.fileupdown.service.AmazonS3Service;
import com.aws.fileupdown.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
public class MetadataServiceImpl implements MetadataService {

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    //remover
    private static String TOKEN_AUTH_HUB = null;

    @Override
    public void uploadLocalFilePutObject() {

        amazonS3.putObject(new PutObjectRequest(bucketName, "carlosTeste3030.csv",
                new File("C://filesToS3/relatorio-CLIENTES-211123.csv")));
    }

    @Override
    public void uploadLocalFileMock() throws IOException {

        File file = new File("C://filesToS3/relatorio-CLIENTES-211124.csv");
        InputStream stream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), String.valueOf(MediaType.APPLICATION_OCTET_STREAM),
                stream);

        upload(multipartFile);
    }

    @Override
    public void upload(MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = bucketName;
        String fileName = String.format("%s", file.getOriginalFilename());

        // Uploading file to s3
        PutObjectResult putObjectResult = amazonS3Service.upload(
                path, fileName, Optional.of(metadata), file.getInputStream());
    }

    @Override
    public S3Object download(String path, String name) {
        S3Object s3Object = null;
        try {
            s3Object = amazonS3Service.download(path, name);
            return s3Object;
        } catch (Exception e) {
            log.error("AWSs3error: " + e.getMessage(), e);
            throw new RuntimeException("AWSs3error: arquivo não encontrado.");
        }
    }

    @Override
    public FileResource getFile(String path, String fileName) throws IOException {
        final var s3Object = amazonS3Service.download(path, fileName);
        final ByteArrayResource byteArrayResource = new ByteArrayResource(s3Object.getObjectContent().readAllBytes());
        return new FileResource(byteArrayResource, "file" +
                UUID.randomUUID() + fileName.split(".", 2)[0]);
    }

    @Override
    public List<FileMetadata> list() {
        List<FileMetadata> metas = new ArrayList<>();
        return metas;
    }

    //remover
    @Override
    public void testarGetToken() {
        System.out.println("tokenInical: " + TOKEN_AUTH_HUB);
        verificarTokenEReconsultar();
        if (LocalTime.now().isAfter(LocalTime.of(10, 48))) {
            resetarToken();
        }
    }

    public synchronized String verificarTokenEReconsultar() {

        if (TOKEN_AUTH_HUB == null) {
            if (TOKEN_AUTH_HUB == null) {
                TOKEN_AUTH_HUB = "123";
                System.out.println("tokenObtido: " + TOKEN_AUTH_HUB);
            }
        }
        return TOKEN_AUTH_HUB;
    }

    public synchronized void resetarToken() {
        TOKEN_AUTH_HUB = null;
        System.out.println("expirou");
    }
}
