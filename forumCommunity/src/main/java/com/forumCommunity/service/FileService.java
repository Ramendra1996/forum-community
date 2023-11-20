package com.forumCommunity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${project.image}")
    private String path;

    public String uploadImage(String path, MultipartFile file) throws IOException {

        //filename
         String name = file.getOriginalFilename();
        //full path
        String filePath =path+ File.separator+name;
        //create folder if not created
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.mkdir();
        }
        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return name;
    }
    public boolean deleteImage(String imageName) {
        Path imagePath = Paths.get(path + imageName);

        try {
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                return true;
            } else {
                return false; // Image not found
            }
        } catch (IOException e) {
            // Log the exception or handle it as needed
            e.printStackTrace();
            return false; // Error deleting image
        }
    }
}
