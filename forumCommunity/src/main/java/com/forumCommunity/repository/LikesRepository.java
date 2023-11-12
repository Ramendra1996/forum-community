package com.forumCommunity.repository;

import com.forumCommunity.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes ,Long> {

}
