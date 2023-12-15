package com.forumCommunity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forumCommunity.customKey.CustomKeyGenerator;
import com.forumCommunity.entity.Post;
import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@CacheConfig(cacheNames = {"post"})
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CustomKeyGenerator customKeyGenerator;
//create post
    @PostMapping
    @CacheEvict(allEntries = true)
    public ResponseEntity<BaseResponse> createPost(@RequestBody Post post){
        return new ResponseEntity<>(new BaseResponse(true,postService.createPost(post)), HttpStatus.CREATED);
    }

   //updatePost
    @PutMapping
    @CachePut(key = "#id")
   public ResponseEntity<BaseResponse> updatePost(@RequestBody Post post){
        return new ResponseEntity<>(new BaseResponse(true,postService.updatePost(post)),HttpStatus.OK);
   }

   @GetMapping
   @Cacheable(keyGenerator = "customKeyGenerator")
   public ResponseEntity<BaseResponse> getAllPost(@RequestParam (value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size",defaultValue = "100")int size,
                                                  @RequestParam(value = "sort",defaultValue = "dateOfModification") String sortBy,
                                                  @RequestParam(value = "topicId",defaultValue = "0") Long topicId,
                                                  @RequestParam(value = "categoryId",defaultValue = "0")Long categoryId,
                                                  @RequestParam(value = "userId",defaultValue = "0")Long userId){
    return new ResponseEntity<>(new BaseResponse(true,postService.getAllPost(page,size,sortBy,topicId,categoryId,userId)),HttpStatus.OK);
   }

    @GetMapping("/all")
   public ResponseEntity<BaseResponse> getAll(){
        return new ResponseEntity<>(new BaseResponse(true,postService.getAll()),HttpStatus.OK);
   }
    @GetMapping("topgainer")
   public ResponseEntity<BaseResponse> getAllTopGainer() throws JsonProcessingException {
        return new ResponseEntity<>(new BaseResponse(true,postService.getAllTopGainer()),HttpStatus.OK);
   }

   @GetMapping("allStock")
   private ResponseEntity<BaseResponse> getStocj(){
        return new ResponseEntity<>(new BaseResponse(true,postService.getStockByWebClient()),HttpStatus.OK);
   }

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> getAllPosts(@RequestParam(value = "page", required = false, defaultValue = "0") int page
            , @RequestParam(value = "size", required = false, defaultValue = "100") int size
            , @RequestParam(value = "sort", defaultValue = "dateOfModification") String sort,
                                                   @RequestParam(value = "topicId", required = false, defaultValue = "0") Long topicId,
                                                   @RequestParam(value = "categoryId", required = false, defaultValue = "0") Long categoryId,
                                                   @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId) {
        return new ResponseEntity<>(new BaseResponse(true, postService.getAllPostWithFilter(page, size, sort, topicId, categoryId, userId)), HttpStatus.OK);
    }
}
