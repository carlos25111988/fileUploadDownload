package com.aws.fileupdown.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.aws.fileupdown.config.files.HttpHeadersUtils;
import com.aws.fileupdown.service.MetadataService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/s3")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AmazonS3EndPoint {

    private MetadataService metadataService;

    @GetMapping
    public ResponseEntity<S3Object> download(String path, String name) {
        return ResponseEntity.ok(metadataService.download(path, name));
    }

    @GetMapping("/file")
    public ResponseEntity<ByteArrayResource> getFile(
            @RequestParam String path,
            @RequestParam String fileName) throws IOException {

        MediaType mediaType = null;

        log.info("tipoArquivo: " + fileName.substring(fileName.indexOf(".")));

        if (fileName.substring(fileName.indexOf(".")).equals(".jpg") ||
                fileName.substring(fileName.indexOf(".")).equals(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        }

        if (fileName.substring(fileName.indexOf(".")).equals(".csv")) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        try {
            final var file = metadataService.getFile(path, fileName);
            return ResponseEntity.ok()
                    .headers(HttpHeadersUtils.header(file))
                    .contentLength(file.getResource().contentLength())
                    .contentType(mediaType)
                    .body(file.getResource());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ocorreu um erro na requisição: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file) throws IOException {
        metadataService.upload(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("uploadLocalFileMock")
    public ResponseEntity<?> uploadLocalFileMock() throws IOException {
        metadataService.uploadLocalFileMock();
        return ResponseEntity.ok().build();
    }

    @PostMapping("uploadLocalFilePutObject")
    public ResponseEntity<?> uploadLocalFileCommons() throws IOException {
        metadataService.uploadLocalFilePutObject();
        return ResponseEntity.ok().build();
    }

    @GetMapping("testarToken")
    public ResponseEntity<?> testarToken() {
        metadataService.testarGetToken();
        return ResponseEntity.ok().build();
    }
}
