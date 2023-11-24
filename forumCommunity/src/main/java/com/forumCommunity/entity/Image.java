package com.forumCommunity.entity;

import com.forumCommunity.model.ObjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String imageUrl;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;
    private  Long objectId;
}
