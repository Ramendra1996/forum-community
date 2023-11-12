package com.forumCommunity.repository;

import com.forumCommunity.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {
    @Query("SELECT t, COUNT(p) FROM Topic t LEFT JOIN Post p ON t.topicId = p.topicId GROUP BY t")
    List<Object[]> findAllTopicsWithPostCount();
}
