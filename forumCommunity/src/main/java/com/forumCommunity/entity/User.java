package com.forumCommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    //private String password;
    @Transient
    private MultipartFile userPic;
    private String userDescription;
    private String userEmail;
    private Date dateOfCreation;
    private Date dateOfModification;

}
