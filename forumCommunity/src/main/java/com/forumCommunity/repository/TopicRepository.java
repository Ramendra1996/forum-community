package com.forumCommunity.repository;

import com.forumCommunity.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {

    long countByTopicId(Long topicId);
}
