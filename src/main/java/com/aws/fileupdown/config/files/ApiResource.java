package com.aws.fileupdown.config.files;

import org.springframework.core.io.ByteArrayResource;

public interface ApiResource {

    ByteArrayResource getResource();

    String getFilename();

}
