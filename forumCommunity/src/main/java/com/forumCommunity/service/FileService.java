package com.forumCommunity.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    @Value("${project.image}")
    private String path;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private HttpServletRequest request;
    public String uploadImage(MultipartFile file) throws IOException {

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
        String path1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(file.getOriginalFilename()).toUriString();
        String contextPath = request.getContextPath();
        String relativePath = "/image/" + file.getOriginalFilename();

        return contextPath + relativePath;

    }

    public byte[] getImage(String filename) throws IOException {
        Path imagePath = Paths.get(path, filename);
        return Files.readAllBytes(imagePath);
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




    public String getUploadDirectory() {
        Resource resource = resourceLoader.getResource("classpath:static/image/");
        try {
            String UPLOAD_DIR = resource.getFile().getAbsolutePath();
            return UPLOAD_DIR;
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

    public FileService() throws IOException {

    }

    public String uploadFile(MultipartFile multipartFile) {
        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(getUploadDirectory() + File.separator + multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            String path1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(multipartFile.getOriginalFilename()).toUriString();
            /*int contextPathLength = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString().length();
            String dynamicSubstring = path1.substring(contextPathLength + 1);*/
          /*  Path fullPath = Paths.get(path1);
            String fileName = fullPath.getFileName().toString();

            return fileName;*/
            String contextPath = request.getContextPath();
            String relativePath = "/image/" + multipartFile.getOriginalFilename();

            return contextPath + relativePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not Uploaded";
    }
 /* public void uploadFile(byte[] fileData,String originalFilename) {
      try {
          // Generate a unique filename or use the original filename
          String filename = generateUniqueFileName(originalFilename); // Implement this method as needed
          Path filePath = Path.of(getUploadDirectory(), filename);

          // Save the file to the specified directory
          Files.write(filePath, fileData);

          // If you want to move the file instead of copying, you can use:
          // Files.move(tempFilePath, filePath, StandardCopyOption.REPLACE_EXISTING);

          System.out.println("File uploaded successfully: " + filePath);
      } catch (IOException e) {
          e.printStackTrace();
          // Handle the exception appropriately (e.g., log, throw custom exception)
      }
  }

    private String generateUniqueFileName(String originalFilename) {
        // Extract file extension (if any) from the original filename
        String fileExtension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex != -1) {
            fileExtension = originalFilename.substring(lastDotIndex);
        }

        // Generate a unique filename using a combination of timestamp and random UUID
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String randomString = UUID.randomUUID().toString().replace("-", "");

        // Combine timestamp, random string, and file extension (if any)
        return "file_" + timestamp + "_" + randomString + fileExtension;
    }*/
}
