package com.aws.fileupdown.service;

import com.amazonaws.services.codecommit.model.FileMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.fileupdown.config.files.impl.FileResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MetadataService {
    void uploadLocalFilePutObject();

    void uploadLocalFileMock() throws IOException;

    void upload(MultipartFile file) throws IOException;

    S3Object download(String path, String name);

    FileResource getFile(String path, String fileName) throws IOException;

    List<FileMetadata> list();

    //remover
    void testarGetToken();
}
