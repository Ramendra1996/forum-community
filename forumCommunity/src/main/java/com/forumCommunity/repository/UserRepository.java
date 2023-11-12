package com.forumCommunity.repository;

import com.forumCommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User ,Long> {


}
