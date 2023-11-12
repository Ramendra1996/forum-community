package com.forumCommunity.util;

import com.forumCommunity.entity.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecifications {

    public static Specification<Post> hasTopicId(Long topicId) {
        return (root, query, criteriaBuilder) ->
                topicId == null || topicId == 0 ?
                        criteriaBuilder.isTrue(criteriaBuilder.literal(true)) :
                        criteriaBuilder.equal(root.get("topicId"), topicId);
    }

    public static Specification<Post> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null || categoryId == 0 ?
                        criteriaBuilder.isTrue(criteriaBuilder.literal(true)) :
                        criteriaBuilder.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Post> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null || userId == 0 ?
                        criteriaBuilder.isTrue(criteriaBuilder.literal(true)) :
                        criteriaBuilder.equal(root.get("userId"), userId);
    }
}
