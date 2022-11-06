package com.eticket.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    List<String> storeListFile(MultipartFile[] files);

    String storeFile(MultipartFile file);
}
