package com.forumCommunity.repository;

import com.forumCommunity.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll ,Long> {
}
