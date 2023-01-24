package com.eticket.infrastructure.file;

import com.eticket.domain.entity.event.Ticket;
import com.eticket.domain.exception.FileStorageException;
import com.eticket.infrastructure.mapper.TicketMap;
import com.eticket.infrastructure.utils.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Value("${upload.dir}")
    private final String uploadDir = "src/main/upload/static/image";

    @Value("${prefix.uri}")
    private String prefixURI;

    @Autowired
    private TicketMap ticketMap;

    @Autowired
    public FileStorageService() {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

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

    public String generateQRCode(Ticket ticket) {
        //data that we want to store in the QR code
        String data = Utils.convertObjectToJson(ticketMap.toTicketInfomation(ticket));
        //path where we want to get QR Code
        String dir = "event/" + ticket.getEvent().getId() + "/" + ticket.getTicketCatalog().getId();
        File ticketQRLocationDir = new File(uploadDir + "/" + dir);
        if (!ticketQRLocationDir.exists()) {
            ticketQRLocationDir.mkdirs();
        }
        String ticketImageName = ticket.getCode() + ".png";
        String path = uploadDir + "/" + dir + "/" + ticketImageName;
        //Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        //generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prefixURI + dir + "/" + ticketImageName;
    }
    
    public void removeQRCode(String qrCodePath) {

    }
}
