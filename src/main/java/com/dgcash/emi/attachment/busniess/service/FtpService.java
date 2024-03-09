package com.dgcash.emi.attachment.busniess.service;


import com.dgcash.emi.attachment.busniess.exceptions.FileNotFoundOnFTPException;
import com.dgcash.emi.attachment.busniess.exceptions.InvalidFileType;
import com.dgcash.emi.attachment.busniess.exceptions.UploadFileException;
import com.dgcash.emi.attachment.common.FileType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dgcash.emi.attachment.common.FileType.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FtpService {

    private final SessionFactory<FTPFile> cachingSessionFactory;
    private final PasswordEncoder passwordEncoder;

    @Value("${profile.merchant.iban.path}")
    private String ibanMerchantPath;

    @Value("${profile.customer.iban.path}")
    private String ibanCustomerPath;
    @Value("${profile.cr.path}")
    private String crPath;
    @Value("${profile.logo.path}")
    private String logoPath;
    @Value("${profile.signatory.path}")
    private String signatoryPath;
    @Value("${${profile.document.path}")
    private String documentPath;
    @Value("${profile.vat.path}")
    private String vatPath;

    public String uploadFile(MultipartFile file, String fileName, String fileType) {
        try {
            FTPClient ftpClient = getFtpClient(cachingSessionFactory.getSession());

            String path = getFilePath(fileType);
            if (!directoryExists(ftpClient, path)) {
                ftpClient.makeDirectory(path);
            }

            boolean storeFile = ftpClient.storeFile(path + fileName, file.getInputStream());
            validateStoreFile(storeFile);
            disconnectFtpClient(ftpClient);
        } catch (Exception e) {
            throw new UploadFileException();
        }
        return fileName;
    }

    @SneakyThrows
    private boolean directoryExists(FTPClient ftpClient, String path) {
        FTPFile[] files = ftpClient.listFiles(path);
        return files != null && files.length > 0 && files[0].isDirectory();
    }

    private String getFilePath(String fileType) {
        switch (FileType.valueOf(StringUtils.upperCase(fileType))) {
            case IBAN_CUSTOMER:
                return ibanCustomerPath;
            case IBAN_MERCHANT:
                return ibanMerchantPath;
            case CR:
                return crPath;
            case LOGO:
                return logoPath;
            case SIGNATORY:
                return signatoryPath;
            case DOCUMENT:
                return documentPath;
            case VAT:
                return vatPath;
            default:
                throw new InvalidFileType();
        }
    }
    private void validateStoreFile(boolean storeFile) {
        Stream.of(storeFile)
                .filter(Boolean::booleanValue)
                .findFirst()
                .orElseThrow(UploadFileException::new);
    }

    @SneakyThrows
    public void downloadFile(String file) {
        getFtpClient(cachingSessionFactory.getSession()).retrieveFile(file, new FileOutputStream(file));
    }

    @SneakyThrows
    public boolean fileExists(String fileToken, String fileType) {
        FTPClient ftpClient = getFtpClient(cachingSessionFactory.getSession());
        FTPFile[] files = getListFiles(ftpClient, getFilePath(fileType) + fileToken);
        disconnectFtpClient(ftpClient);
        return files.length > 0;
    }


    @SneakyThrows
    public byte[] viewFile(String fileName, String fileType) {
        FTPClient ftpClient = getFtpClient(cachingSessionFactory.getSession());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.retrieveFile(getFilePath(fileType) + fileName, outputStream);
        byte[] fileBytes = outputStream.toByteArray();
        disconnectFtpClient(ftpClient);
        return fileBytes;
    }

    private static void disconnectFtpClient(FTPClient ftpClient) throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    private static FTPFile[] getListFiles(FTPClient ftpClient, String currentPath) throws IOException {
        return ftpClient.listFiles(currentPath);
    }

    @SneakyThrows
    private FTPClient getFtpClient(Session<FTPFile> session) {
        FTPClient ftpClient = (FTPClient) session.getClientInstance();
        ftpClient.setFileType(FTPSClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setRestartOffset(0L);
        return ftpClient;
    }
}
