package com.forumCommunity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Post;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.entity.User;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.model.*;
import com.forumCommunity.repository.*;
import com.sun.net.httpserver.Headers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;


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
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


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

     /* public TopGainer getAllTopGainer() throws JsonProcessingException {
        TopGainer topGainer = new TopGainer();

          ResponseEntity<String> exchange = restTemplate.exchange("https://intradayscreener.com/api/trackStocks/cash", HttpMethod.GET, new HttpEntity<>(new Headers()), String.class);
          String s = exchange.getBody();
          JSONObject jsonObject = new JSONObject(s);

          topGainer.setSymbol(jsonObject.getString("symbol"));
          topGainer.setLtp(jsonObject.getLong("ltp"));
          topGainer.setPriceChangePct(jsonObject.getLong("priceChangePct"));
          topGainer.setOiChangePct(jsonObject.getLong("oiChangePct"));

          // Add other properties if needed

          // Finally, return the populated TopGainer object
          return topGainer;

      }*/

    public TopGainer getAllTopGainer() {
        ResponseEntity<String> exchange = restTemplate.exchange(
                "https://intradayscreener.com/api/trackStocks/cash",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        String responseBody = exchange.getBody();

        if (responseBody != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                // Assuming the topGainers array is nested under a key named "topGainers"
                JsonNode topGainersNode = rootNode.get("topGainers");

                // If topGainersNode is an array, you can iterate through it
                List<TopGainerItem> topGainers = new ArrayList<>();
                for (JsonNode topGainerNode : topGainersNode) {
                    TopGainerItem topGainerItem = new TopGainerItem();
                    topGainerItem.setSymbol(topGainerNode.get("symbol").asText());
                    topGainerItem.setLtp(topGainerNode.get("ltp").asDouble());
                    topGainerItem.setPriceChangePct(topGainerNode.get("priceChangePct").asDouble());
                    topGainerItem.setOiChangePct(topGainerNode.get("oiChangePct").asDouble());

                    // Add other properties if needed

                    topGainers.add(topGainerItem);
                }

                // Set the list of topGainers in the TopGainer object
                TopGainer topGainer = new TopGainer();
                topGainer.setTopGainers(topGainers);

                return topGainer;
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
        }

        // Handle the case where the response body is null or parsing fails
        return null;
    }
}



