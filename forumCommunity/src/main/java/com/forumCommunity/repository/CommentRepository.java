package com.forumCommunity.repository;


import com.forumCommunity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    long countByPostId(Long postId);

    @Query("SELECT c.postId, COUNT(c) FROM Comment c WHERE c.postId IN :postIds GROUP BY c.postId")
    Map<Long, Long> countCommentsByPostIds(@Param("postIds") List<Long> postIds);

}
