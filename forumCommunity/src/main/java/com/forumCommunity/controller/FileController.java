package com.forumCommunity.controller;

import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService  fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping
    public ResponseEntity<BaseResponse>fileUpload(@RequestParam("image")MultipartFile image) throws IOException {
        return new ResponseEntity<>(new BaseResponse(true,fileService.uploadImage(path,image)), HttpStatus.OK);
    }

    @DeleteMapping("/{imageName}")
    public ResponseEntity<String> deleteImage(@PathVariable String imageName) {
        boolean deleted = fileService.deleteImage(imageName);

        if (deleted) {
            return ResponseEntity.ok("Image deleted successfully");
        } else {
            return ResponseEntity.notFound().build(); // Image not found
        }
    }
}
