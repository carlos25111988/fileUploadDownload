package com.aws.fileupdown.config.files.impl;

import com.aws.fileupdown.config.files.ApiResource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@AllArgsConstructor
public class FileResource implements ApiResource {

    private ByteArrayResource resource;
    private String filename;

    @Override
    public ByteArrayResource getResource() {
        return this.resource;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }
}

