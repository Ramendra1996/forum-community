package com.forumCommunity.repository;

import com.forumCommunity.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface LikesRepository extends JpaRepository<Likes ,Long> {
   long countByPostId(Long postId);

   @Query("SELECT l.postId, COUNT(l) FROM Likes l WHERE l.postId IN :postIds GROUP BY l.postId")
   Map<Long, Long> countLikesByPostIds(@Param("postIds") List<Long> postIds);
}
