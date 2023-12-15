package com.forumCommunity.model;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private Long userId;
    private Long rePostId;
    private Long topicId;
    private Long categoryId;
    private  String postContent;
    private Long reported;
    private Date dateOfCreation;
    private Date dateOfModification;
    private Repost repost;
    private Long likesCount;
    private Long commentCount;
    private String userName;
    private String topicName;
    private String categoryName;

}
