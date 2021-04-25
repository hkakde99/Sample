package com.spring.boot.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.spring.boot.config.FileStorageProperties;
import com.spring.boot.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService{
	private final Path fileStorageLocation; // file path --> 

    static {
    	System.out.println("FileStorage Service -- initialized..");
    }
    
    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation); // this line is to store files..
        } catch (Exception ex) {
        	System.out.println("Could not create the directory where the uploaded files will be stored."+ex.getMessage());
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
            	System.out.println("Sorry! Filename contains invalid path sequence ");
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            System.out.println("targetLocation -->"+targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
        	System.out.println("Could not store file "+ex.getMessage());
        }
		return fileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
            	System.out.println("File not found");
            }
        } catch (MalformedURLException ex) {
        	System.out.println("File not found " +ex.getMessage());
        }
		return null;
    }

}
