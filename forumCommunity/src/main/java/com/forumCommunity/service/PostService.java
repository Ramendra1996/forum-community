package com.forumCommunity.service;

import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Post;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.entity.User;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TopicRepository topicRepository;


    public Post createPost(Post post){
        post.setDateOfCreation(new Date());
        post.setDateOfModification(new Date());
        return postRepository.save(post);
    }

    public Post updatePost(Post post){
        Optional<Post> postObj = postRepository.findById(post.getPostId());
        if (postObj.isEmpty()){
            throw new ForumCommunityServiceException("Post id Not Valid", HttpStatus.OK);
        }
        postObj.get().setDateOfModification(new Date());
        postObj.get().setReported(post.getReported());
        postObj.get().setPostContent(post.getPostContent());
        return postRepository.save(postObj.get());
    }

    //getAll post
/*   public List<Post> getAllPost(int page, int size, String sortBy){
       List<Post> allPost = postRepository.findAll(PageRequest.of(page,size, Sort.by(sortBy))).getContent();
       allPost.forEach(post -> {
           Optional<User> user = userRepository.findById(post.getUserId());
           Optional<Topic> topic = topicRepository.findById(post.getTopicId());
           Optional<Category> category = categoryRepository.findById(post.getCategoryId());
           post.setLikesCount(findAllPostsWithLikeCount().stream().count());
           post.setCommentCount(findAllPostsWithCommentCount().stream().count());
           post.setUserName(user.get().getUserName());
           post.setTopicName(topic.get().getTopicName());
           post.setCategoryName(category.get().getCategoryName());
       });
       return allPost;
   }*/

//likes count each post
   public List<Post> findAllPostsWithLikeCount() {
        List<Object[]> results = postRepository.findAllPostsWithLikeCount();
        List<Post> postWithLikesCounts = new ArrayList<>();
        for (Object[] result : results) {
            Post post = (Post) result[0];
            Long likesCount = (Long) result[1];
            post.setLikesCount(likesCount);
            postWithLikesCounts.add(post);
        }
        return postWithLikesCounts;
    }
    // comment count each post
    public List<Post> findAllPostsWithCommentCount() {
        List<Object[]> results = postRepository.findAllPostsWithCommentCount();
        List<Post> postWithCommentCounts = new ArrayList<>();
        for (Object[] result : results) {
            Post post = (Post) result[0];
            Long commentCount = (Long) result[1];
            post.setLikesCount(commentCount);
            postWithCommentCounts.add(post);
        }
        return postWithCommentCounts;
    }


    public Page<Post> getAllPost(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);

        Page<Post> allPost = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy)));
        allPost.getContent().forEach(post -> {
            Optional<User> user = userRepository.findById(post.getUserId());
            Optional<Topic> topic = topicRepository.findById(post.getTopicId());
            Optional<Category> category = categoryRepository.findById(post.getCategoryId());
            post.setLikesCount(findAllPostsWithLikeCount().stream().count());
            post.setCommentCount(findAllPostsWithCommentCount().stream().count());
            post.setUserName(user.get().getUserName());
            post.setTopicName(topic.get().getTopicName());
            post.setCategoryName(category.get().getCategoryName());
        });

        return allPost;
    }
}
