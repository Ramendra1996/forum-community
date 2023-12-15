package com.forumCommunity.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Post;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.entity.User;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.model.*;
import com.forumCommunity.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    @Autowired
    private WebClient webClient;



    public Post createPost(Post post) {
        post.setDateOfCreation(new Date());
        post.setDateOfModification(new Date());
        post.setStatus(EnumStatus.ACTIVE);
        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        Optional<Post> postObj = postRepository.findById(post.getPostId());
        if (postObj.isEmpty()) {
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

    private Long likeCountByPostId(Long postId) {
        return likesRepository.countByPostId(postId);
    }

    private Long commentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    public List<PostResponse> getAll() {
        List<Post> all = postRepository.findAll();
        List<PostResponse> postResponseList = new ArrayList<>();
        all.forEach(post -> {
            PostResponse postResponse = new PostResponse();
            if (post.getRePostId() != null) {
                Optional<Post> rPost = postRepository.findById(post.getRePostId());
                Repost repost = new Repost();
                repost.setRePostId(rPost.get().getRePostId());
                repost.setPostId(rPost.get().getPostId());
                repost.setUserId(rPost.get().getUserId());
                repost.setCategoryId(rPost.get().getCategoryId());
                repost.setTopicId(rPost.get().getTopicId());
                repost.setDateOfCreation(rPost.get().getDateOfCreation());
                repost.setDateOfModification(rPost.get().getDateOfModification());
                repost.setPostContent(rPost.get().getPostContent());
                postResponse.setRepost(repost);
            }
            postResponse.setPostId(post.getPostId());
            postResponse.setUserId(post.getUserId());
            postResponse.setCategoryId(post.getCategoryId());
            postResponse.setTopicId(post.getTopicId());
            postResponse.setDateOfCreation(post.getDateOfCreation());
            postResponse.setDateOfModification(post.getDateOfModification());
            postResponse.setPostContent(post.getPostContent());
            Optional<Topic> topic = topicRepository.findById(post.getTopicId());
            Optional<User> user = userRepository.findById(post.getUserId());
            Optional<Category> category = categoryRepository.findById(post.getCategoryId());
            postResponse.setCategoryName(category.get().getCategoryName());
            postResponse.setUserName(user.get().getUserName());
            postResponse.setTopicName(topic.get().getTopicName());
            postResponse.setLikesCount(likesRepository.countByPostId(post.getPostId()));
            postResponse.setCommentCount(commentRepository.countByPostId(post.getPostId()));
            postResponseList.add(postResponse);
        });
      //  Page<PostResponse> m = new PageImpl<>(postResponseList);
        return postResponseList;
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

/*    public TopGainer getAllTopGainer() {
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
                JsonNode topLosersNode =  rootNode.get("topLoosers");
                // If topGainersNode is an array, you can iterate through it
                List<Item> topGainers = new ArrayList<>();
                for (JsonNode topGainerNode : topGainersNode) {
                    Item top = new Item();
                    top.setSymbol(topGainerNode.get("symbol").asText());
                    top.setLtp(topGainerNode.get("ltp").asDouble());
                    top.setPriceChangePct(topGainerNode.get("priceChangePct").asDouble());
                    top.setOiChangePct(topGainerNode.get("oiChangePct").asDouble());
                    // Add other properties if needed
                    topGainers.add(top);
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
    }*/

    public TopGainerAndLoosers getAllTopGainer() {
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

                JsonNode topGainersNode = rootNode.get("topGainers");
                JsonNode topLosersNode = rootNode.get("topLoosers");

                List<Item> topGainers = mapJsonNodeToItems(topGainersNode);
                List<Item> topLosers = mapJsonNodeToItems(topLosersNode);

                TopGainerAndLoosers topGainer = new TopGainerAndLoosers();
                topGainer.setTopGainers(topGainers);
                topGainer.setTopLosers(topLosers);

                return topGainer;
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
        }

        return null;
    }

    private List<Item> mapJsonNodeToItems(JsonNode jsonNode) {
        List<Item> items = new ArrayList<>();

        for (JsonNode itemNode : jsonNode) {
            Item item = new Item();
            item.setSymbol(itemNode.get("symbol").asText());
            item.setLtp(itemNode.get("ltp").asDouble());
            item.setPriceChangePct(itemNode.get("priceChangePct").asDouble());
            item.setOiChangePct(itemNode.get("oiChangePct").asDouble());
            // Add other properties if needed
            items.add(item);
        }
        return items;
    }

    public TopGainerAndLoosers getAllTopGainer1() {
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

                JsonNode topGainersNode = rootNode.get("topGainers");
                JsonNode topLosersNode = rootNode.get("topLosers");

                List<Item> topGainers = new ArrayList<>();
                for (JsonNode topGainerNode : topGainersNode) {
                    Item top = new Item(
                            topGainerNode.get("symbol").asText(),
                            topGainerNode.get("ltp").asDouble(),
                            topGainerNode.get("priceChangePct").asDouble(),
                            topGainerNode.get("oiChangePct").asDouble()
                    );
                    // Add other properties if needed
                    topGainers.add(top);
                }

                List<Item> topLosers = new ArrayList<>();
                for (JsonNode topLoserNode : topLosersNode) {
                    Item top = new Item(
                            topLoserNode.get("symbol").asText(),
                            topLoserNode.get("ltp").asDouble(),
                            topLoserNode.get("priceChangePct").asDouble(),
                            topLoserNode.get("oiChangePct").asDouble()
                    );
                    // Add other properties if needed
                    topLosers.add(top);
                }

                TopGainerAndLoosers topGainer = new TopGainerAndLoosers();
                topGainer.setTopGainers(topGainers);
                topGainer.setTopLosers(topLosers);
                return topGainer;
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
        }
        return null;
    }


    public String getStock() {
        ResponseEntity<String> exchange = restTemplate.exchange(
                "https://intradayscreener.com/api/trackStocks/cash",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        String responseBody = exchange.getBody();
        return responseBody;
    }

    public String getStockByWebClient(){
        String block = webClient.get().uri("https://intradayscreener.com/api/trackStocks/cash").accept(org.springframework.http.MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(String.class)
                .block();
        return block;
    }


    public Page<PostResponse> getAllPostWithFilter1(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy).descending()));
        List<PostResponse> postResponseList = new ArrayList<>();
        allPosts.getContent().forEach(post -> {
            if (post.getStatus().equals(EnumStatus.ACTIVE)) {
                PostResponse postResponse = new PostResponse();
                Optional<User> user = userRepository.findById(post.getUserId());
                Optional<Topic> topic = topicRepository.findById(post.getTopicId());
                Optional<Category> category = categoryRepository.findById(post.getCategoryId());
                if (post.getRePostId() != null) {
                    Optional<Post> repostObject = postRepository.findById(post.getRePostId());
                    Optional<Topic> topicObject = topicRepository.findById(repostObject.get().getTopicId());
                    Optional<User> userObject = userRepository.findById(repostObject.get().getUserId());
                    Optional<Category> categoryObject = categoryRepository.findById(repostObject.get().getCategoryId());
                    Repost repost = new Repost();
                    repost.setPostId(repostObject.get().getPostId());
                    repost.setRePostId(repostObject.get().getRePostId());
                    repost.setUserId(userObject.get().getUserId());
                    repost.setCategoryId(categoryObject.get().getCategoryId());
                    repost.setTopicId(topicObject.get().getTopicId());
                    repost.setTopicName(topicObject.get().getTopicName());
                    repost.setUserName(userObject.get().getUserName());
                    repost.setCategoryName(categoryObject.get().getCategoryName());
                    repost.setDateOfCreation(repostObject.get().getDateOfCreation());
                    repost.setDateOfModification(repostObject.get().getDateOfModification());
                    repost.setPostContent(repostObject.get().getPostContent());
                    postResponse.setRepost(repost);
                }
                postResponse.setPostId(post.getPostId());
                postResponse.setTopicId(post.getTopicId());
                postResponse.setPostContent(post.getPostContent());
                postResponse.setUserId(post.getUserId());
                postResponse.setCategoryId(post.getCategoryId());
                postResponse.setRePostId(post.getRePostId());
                postResponse.setDateOfCreation(post.getDateOfCreation());
                postResponse.setDateOfModification(post.getDateOfModification());
                postResponse.setUserName(user.get().getUserName());
                postResponse.setCategoryName(category.get().getCategoryName());
                postResponse.setTopicName(topic.get().getTopicName());
                postResponse.setLikesCount(likesRepository.countByPostId(post.getPostId()));
                postResponse.setCommentCount(commentRepository.countByPostId(post.getRePostId()));
                postResponseList.add(postResponse);
            }
        });
        Page<PostResponse> postPage = new PageImpl<>(postResponseList);
        return postPage;

    }

  /*  public Page<PostResponse> getAllPostWithFilter(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy).descending()));

        List<PostResponse> postResponseList = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseList, allPosts.getPageable(), allPosts.getTotalElements());
    }

    private PostResponse mapToPostResponse(Post post) {
        Optional<User> user = userRepository.findById(post.getUserId());
        Optional<Topic> topic = topicRepository.findById(post.getTopicId());
        Optional<Category> category = categoryRepository.findById(post.getCategoryId());

        PostResponse postResponse = new PostResponse();
        postResponse.setPostId(post.getPostId());
        postResponse.setTopicId(post.getTopicId());
        postResponse.setPostContent(post.getPostContent());
        postResponse.setUserId(post.getUserId());
        postResponse.setCategoryId(post.getCategoryId());
        postResponse.setRePostId(post.getRePostId());
        postResponse.setDateOfCreation(post.getDateOfCreation());
        postResponse.setDateOfModification(post.getDateOfModification());
        postResponse.setUserName(user.get().getUserName());
        postResponse.setCategoryName(category.get().getCategoryName());
        postResponse.setTopicName(topic.get().getTopicName());
        postResponse.setLikesCount(likesRepository.countByPostId(post.getPostId()));
        postResponse.setCommentCount(commentRepository.countByPostId(post.getRePostId()));

        if (post.getRePostId() != null) {
            Optional<Post> repostObject = postRepository.findById(post.getRePostId());
            Repost repost = mapToRepost(repostObject.get());
            postResponse.setRepost(repost);
        }

        return postResponse;
    }

    private Repost mapToRepost(Post repostObject) {
        // Map the fields from repostObject to Repost
        Repost repost = new Repost();
        repost.setPostId(repostObject.getPostId());
        repost.setRePostId(repostObject.getRePostId());
        repost.setUserId(repostObject.getUserId());
        repost.setCategoryId(repostObject.getCategoryId());
        repost.setTopicId(repostObject.getTopicId());

        Optional<User> userObject = userRepository.findById(repostObject.getUserId());
        Optional<Category> categoryObject = categoryRepository.findById(repostObject.getCategoryId());
        Optional<Topic> topicObject = topicRepository.findById(repostObject.getTopicId());
        repost.setTopicName(topicObject.get().getTopicName());
        repost.setUserName(userObject.get().getUserName());
        repost.setCategoryName(categoryObject.get().getCategoryName());
        repost.setDateOfCreation(repostObject.getDateOfCreation());
        repost.setDateOfModification(repostObject.getDateOfModification());
        repost.setPostContent(repostObject.getPostContent());
        return repost;
    }*/

 /*   public Page<PostResponse> getAllPostWithFilter(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy).descending()));

        List<Long> postIds = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(Post::getPostId)
                .collect(Collectors.toList());

        Map<Long, Long> likesCountMap = likesRepository.countLikesByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentRepository.countCommentsByPostIds(postIds);

        List<PostResponse> postResponseList = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(post -> {
                    PostResponse postResponse = new PostResponse();
                    // Set other post details as before

                    postResponse.setLikesCount(likesCountMap.getOrDefault(post.getPostId(), 0L));
                    postResponse.setCommentCount(commentCountMap.getOrDefault(post.getPostId(), 0L));

                    return postResponse;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseList, PageRequest.of(page, size), allPosts.getTotalElements());
    }*/

 /*   public Page<PostResponse> getAllPostWithFilter(int page, int size, String sortBy, Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortBy).descending()));
        List<Long> postIds = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(Post::getPostId)
                .collect(Collectors.toList());
        Map<Long, Long> likesCountMap = likesRepository.countLikesByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentRepository.countCommentsByPostIds(postIds);
        List<PostResponse> postResponseList = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(post -> {
                    PostResponse postResponse = new PostResponse();
                    // Set other post details as before
                    postResponse.setLikesCount(likesCountMap.getOrDefault(post.getPostId(), 0L));
                    postResponse.setCommentCount(commentCountMap.getOrDefault(post.getPostId(), 0L));

                    return postResponse;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseList, PageRequest.of(page, size), allPosts.getTotalElements());
    }*/

   /* public Page<PostResponse> getAllPostWithFilter(int page, int size, String sortBy,
                                                   Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec,
                PageRequest.of(page, size, Sort.by(sortBy).descending()));

        List<Long> postIds = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(Post::getPostId)
                .collect(Collectors.toList());

        //Map<Long, Long> likesCountMap = countLikesByPostIds(postIds);
      //  Map<Long, Long> commentCountMap = countCommentsByPostIds(postIds);
        Map<Long, Long> likesCountMap = likesRepository.countLikesByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentRepository.countCommentsByPostIds(postIds);

        List<PostResponse> postResponseList = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(post -> {
                    PostResponse postResponse = new PostResponse();
                    postResponse.setPostId(post.getPostId());
                    postResponse.setTopicId(post.getTopicId());
                    postResponse.setPostContent(post.getPostContent());
                    postResponse.setUserId(post.getUserId());
                    postResponse.setCategoryId(post.getCategoryId());
                    postResponse.setRePostId(post.getRePostId());
                    postResponse.setDateOfCreation(post.getDateOfCreation());
                    postResponse.setDateOfModification(post.getDateOfModification());

                    userRepository.findById(post.getUserId()).map(User::getUserName).ifPresent(postResponse::setUserName);
                    topicRepository.findById(post.getTopicId()).map(Topic::getTopicName).ifPresent(postResponse::setTopicName);
                    categoryRepository.findById(post.getCategoryId()).map(Category::getCategoryName).ifPresent(postResponse::setCategoryName);

                    postResponse.setLikesCount(likesCountMap.getOrDefault(post.getPostId(), 0L));
                    postResponse.setCommentCount(commentCountMap.getOrDefault(post.getPostId(), 0L));

                    if (post.getRePostId() != null) {
                        Post repostObject = postRepository.findById(post.getRePostId()).orElse(null);
                        if (repostObject != null) {
                            Repost repost = new Repost();
                            repost.setPostId(repostObject.getPostId());
                            repost.setRePostId(repostObject.getRePostId());
                            repost.setUserId(repostObject.getUserId());
                            repost.setCategoryId(repostObject.getCategoryId());
                            repost.setTopicId(repostObject.getTopicId());
                            repost.setTopicName(topicRepository.findById(repostObject.getTopicId()).map(Topic::getTopicName).orElse(null));
                            repost.setUserName(userRepository.findById(repostObject.getUserId()).map(User::getUserName).orElse(null));
                            repost.setCategoryName(categoryRepository.findById(repostObject.getCategoryId()).map(Category::getCategoryName).orElse(null));
                            repost.setDateOfCreation(repostObject.getDateOfCreation());
                            repost.setDateOfModification(repostObject.getDateOfModification());
                            repost.setPostContent(repostObject.getPostContent());
                            postResponse.setRepost(repost);
                        }
                    }

                    return postResponse;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseList, PageRequest.of(page, size), allPosts.getTotalElements());
    }
*/

    public Page<PostResponse> getAllPostWithFilter(int page, int size, String sortBy,
                                                   Long topicId, Long categoryId, Long userId) {
        Specification<Post> spec = postRepository.buildSpecification(topicId, categoryId, userId);
        Page<Post> allPosts = postRepository.findAll(spec,
                PageRequest.of(page, size, Sort.by(sortBy).descending()));

        List<Long> postIds = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(Post::getPostId)
                .collect(Collectors.toList());

        // Fetching user, topic, and category data in bulk
        Map<Long, String> userNameMap = userRepository.findAllById(allPosts.getContent().stream().map(Post::getUserId).collect(Collectors.toList()))
               .stream().collect(Collectors.toMap(User::getUserId, User::getUserName));

        Map<Long, String> topicNameMap = topicRepository.findAllById(allPosts.getContent().stream().map(Post::getTopicId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Topic::getTopicId, Topic::getTopicName));

        Map<Long, String> categoryNameMap = categoryRepository.findAllById(allPosts.getContent().stream().map(Post::getCategoryId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryName));

        // Counting likes and comments
        Map<Long, Long> likesCountMap = likesRepository.countLikesByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentRepository.countCommentsByPostIds(postIds);

        List<PostResponse> postResponseList = allPosts.getContent().stream()
                .filter(post -> post.getStatus().equals(EnumStatus.ACTIVE))
                .map(post -> {
                    PostResponse postResponse = new PostResponse();
                    postResponse.setPostId(post.getPostId());
                    postResponse.setTopicId(post.getTopicId());
                    postResponse.setPostContent(post.getPostContent());
                    postResponse.setUserId(post.getUserId());
                    postResponse.setCategoryId(post.getCategoryId());
                    postResponse.setRePostId(post.getRePostId());
                    postResponse.setDateOfCreation(post.getDateOfCreation());
                    postResponse.setDateOfModification(post.getDateOfModification());

                    // Using Optional's ifPresent with method reference
                    postResponse.setUserName(userNameMap.get(post.getUserId()));
                    postResponse.setTopicName(topicNameMap.get(post.getTopicId()));
                    postResponse.setCategoryName(categoryNameMap.get(post.getCategoryId()));

                    postResponse.setLikesCount(likesCountMap.getOrDefault(post.getPostId(), 0L));
                    postResponse.setCommentCount(commentCountMap.getOrDefault(post.getPostId(), 0L));

                    // Extracted method for mapping Repost
                    if (post.getRePostId() != null) {
                        Post repostObject = postRepository.findById(post.getRePostId()).orElse(null);
                        if (repostObject != null) {
                            postResponse.setRepost(mapRepost(repostObject));
                        }
                    }

                    return postResponse;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(postResponseList, PageRequest.of(page, size), allPosts.getTotalElements());
    }

    // Extracted method for mapping Repost
    private Repost mapRepost(Post repostObject) {


        Repost repost = new Repost();
        repost.setPostId(repostObject.getPostId());
        repost.setRePostId(repostObject.getRePostId());
        repost.setUserId(repostObject.getUserId());
        repost.setCategoryId(repostObject.getCategoryId());
        repost.setTopicId(repostObject.getTopicId());
      //  repost.setTopicName(topicRepository.findById(repostObject.getTopicId()).map(Topic::getTopicName).orElse(null));
      //  repost.setUserName(userRepository.findById(repostObject.getUserId()).map(User::getUserName).orElse(null));
      //  repost.setCategoryName(categoryRepository.findById(repostObject.getCategoryId()).map(Category::getCategoryName).orElse(null));
     //   repost.setTopicName(mapEntityNameById(repostObject.getTopicId(), id -> topicRepository.findById(id).map(Topic::getTopicName)));
     //   repost.setUserName(mapEntityNameById(repostObject.getUserId(), id -> userRepository.findById(id).map(User::getUserName)));
    //    repost.setCategoryName(mapEntityNameById(repostObject.getCategoryId(), id -> categoryRepository.findById(id).map(Category::getCategoryName)));
        repost.setDateOfCreation(repostObject.getDateOfCreation());
        repost.setDateOfModification(repostObject.getDateOfModification());
        repost.setPostContent(repostObject.getPostContent());
        return repost;
    }
    private String mapEntityNameById(Long entityId, Function<Long, Optional<String>> nameRetrievalFunction) {
        return nameRetrievalFunction.apply(entityId).orElse(null);
    }


}

