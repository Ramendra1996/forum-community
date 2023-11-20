package com.forumCommunity.entity;

import com.forumCommunity.model.EnumStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private Long userId;
    private Long topicId;
    private Long rePostId;
    private Long categoryId;
    private  String postContent;
    private Long reported;
    private Date dateOfCreation;
    private Date dateOfModification;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumStatus status;
    @Transient
    private Long likesCount;
    @Transient
    private Long commentCount;
    @Transient
    private String userName;
    @Transient
    private String topicName;
    @Transient
    private String categoryName;
}

