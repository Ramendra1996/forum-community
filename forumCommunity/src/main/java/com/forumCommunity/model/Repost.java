package com.forumCommunity.model;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Repost {
    private Long postId;
    private Long userId;
    private Long topicId;
    private Long rePostId;
    private Long categoryId;
    private  String postContent;
    private Long reported;
    private Date dateOfCreation;
    private Date dateOfModification;
    private Long likesCount;
    private Long commentCount;
    private String userName;
    private String topicName;
    private String categoryName;
}
