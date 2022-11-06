package com.eticket.domain.service;

import com.eticket.domain.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    @Value("${upload.dir}")
    private final String uploadDir = "src/main/upload/static/image";

    @Value("${prefix.uri}")
    private String prefixURI;

    @Autowired
    public FileStorageServiceImpl() {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public List<String> storeListFile(MultipartFile[] files) {
        List<String> listFileURI = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String fileURI = storeFile(files[i]);
            if (!fileURI.isEmpty()) {
                listFileURI.add(fileURI);
            }
        }
        return listFileURI;
    }

    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String[] fileNamePart = fileName.split("\\.");
        String postFix = fileNamePart[fileNamePart.length - 1];
        if (!postFix.equals("jpg") && !postFix.equals("jpeg") && !postFix.equals("png")) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMddhhmmssSSS_");
        fileName = format.format(new Date()) + fileName;
        String relativePath = prefixURI + fileName;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return relativePath;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
