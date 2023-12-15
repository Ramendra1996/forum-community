package com.forumCommunity.repository;

import com.forumCommunity.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {

    long countByTopicId(Long topicId);

    @Query("SELECT t, c.categoryName, COUNT(p) " +
            "FROM Topic t " +
            "JOIN Category c ON t.categoryId = c.categoryId " +
            "LEFT JOIN Post p ON t.topicId = p.topicId " +
            "GROUP BY t.topicId, c.categoryName")
    List<Object[]> findAllTopicsWithCategoryAndPostCount();

     List<Topic>findByCategoryId(Long categoryId);
}
