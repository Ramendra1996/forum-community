package com.forumCommunity.service;

import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Post;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.entity.User;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.model.EnumStatus;
import com.forumCommunity.model.PostResponse;
import com.forumCommunity.model.Repost;
import com.forumCommunity.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CommentRepository commentRepository;


    public Post createPost(Post post){
        post.setDateOfCreation(new Date());
        post.setDateOfModification(new Date());
        post.setStatus(EnumStatus.ACTIVE);
        return postRepository.save(post);
    }

    public Post updatePost(Post post){
        Optional<Post> postObj = postRepository.findById(post.getPostId());
        if (postObj.isEmpty()){
            throw new ForumCommunityServiceException("Post id Not Valid", HttpStatus.OK);
        }
        postObj.get().setDateOfModification(new Date());
        postObj.get().setPostContent(post.getPostContent());
        return postRepository.save(postObj.get());
    }
    public Page<Post> getAllPost(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPost = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy)));
        allPost.getContent().forEach(post -> {
            Optional<User> user = userRepository.findById(post.getUserId());
            Optional<Topic> topic = topicRepository.findById(post.getTopicId());
            Optional<Category> category = categoryRepository.findById(post.getCategoryId());
            post.setLikesCount(likeCountByPostId(post.getPostId()));
            post.setCommentCount(commentCountByPostId(post.getPostId()));
            post.setUserName(user.get().getUserName());
            post.setTopicName(topic.get().getTopicName());
            post.setCategoryName(category.get().getCategoryName());
        });
        return allPost;
    }

    private Long likeCountByPostId(Long postId){
        return likesRepository.countByPostId(postId);
    }

    private Long commentCountByPostId(Long postId){
        return  commentRepository.countByPostId(postId);
    }
     public Page<PostResponse> getAll(){
         List<Post> all = postRepository.findAll();

         List<PostResponse> postResponseList = new ArrayList<>();


         all.forEach(post -> {
             PostResponse postResponse = new PostResponse();
             postResponse.setPostId(post.getPostId());
             postResponse.setUserId(post.getUserId());
             postResponse.setPostContent(post.getPostContent());
             if(post.getRePostId()!=null){
                 Repost repost = new Repost();
                 repost.setRePostId(post.getRePostId());
                 repost.setUserId(post.getUserId());
                 repost.setPostContent(post.getPostContent());
                 postResponse.setRepost(repost);
             }
             postResponseList.add(postResponse);
         });
         Page<PostResponse> m = new PageImpl<>(postResponseList);


         return m;
     }



}
