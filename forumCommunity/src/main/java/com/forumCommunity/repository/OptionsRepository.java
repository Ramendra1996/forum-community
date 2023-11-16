package com.forumCommunity.repository;

import com.forumCommunity.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionsRepository extends JpaRepository<Options ,Long> {

    List<Options> findByPollId(Long pollId);
}
