package com.eticket.domain.service;

import com.eticket.domain.entity.event.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    List<String> storeListFile(MultipartFile[] files);

    String storeFile(MultipartFile file);

    String generateQRCode(Ticket ticket);

    void removeQRCode(String qrCodePath);
}
