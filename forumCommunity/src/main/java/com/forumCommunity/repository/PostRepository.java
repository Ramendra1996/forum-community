package com.forumCommunity.repository;

import com.forumCommunity.entity.Post;
import com.forumCommunity.util.PostSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> , JpaSpecificationExecutor<Post> {



    default Specification<Post> buildSpecification(Long topicId, Long categoryId, Long userId) {
        return Specification
                .where(PostSpecifications.hasTopicId(topicId))
                .and(PostSpecifications.hasCategoryId(categoryId))
                .and(PostSpecifications.hasUserId(userId));
    }

   long countByTopicId(Long topicId);

    @Query("SELECT p.topicId, COUNT(p) FROM Post p WHERE p.topicId IN :topicIds GROUP BY p.topicId")
    List<Object[]> countPostByTopicIds(@Param("topicIds") List<Long> topicIds);


}
