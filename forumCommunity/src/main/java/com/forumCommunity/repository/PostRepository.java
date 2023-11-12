package com.forumCommunity.repository;

import com.forumCommunity.entity.Post;
import com.forumCommunity.util.PostSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> , JpaSpecificationExecutor<Post> {

    @Query("SELECT p, COUNT(l) FROM Post p LEFT JOIN Likes l ON p.postId = l.postId GROUP BY p")
    List<Object[]> findAllPostsWithLikeCount();

    @Query("SELECT p, COUNT(c) FROM Post p LEFT JOIN Comment c ON p.postId = c.postId GROUP BY p")
    List<Object[]> findAllPostsWithCommentCount();


    default Specification<Post> buildSpecification(Long topicId, Long categoryId, Long userId) {
        return Specification
                .where(PostSpecifications.hasTopicId(topicId))
                .and(PostSpecifications.hasCategoryId(categoryId))
                .and(PostSpecifications.hasUserId(userId));
    }





}
