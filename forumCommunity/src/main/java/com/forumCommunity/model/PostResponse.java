package com.forumCommunity.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
