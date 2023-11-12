package com.forumCommunity.controller;

import com.forumCommunity.entity.Post;
import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
//create post
    @PostMapping
    public ResponseEntity<BaseResponse> createPost(@RequestBody Post post){
        return new ResponseEntity<>(new BaseResponse(true,postService.createPost(post)), HttpStatus.CREATED);
    }

   //updatePost
    @PutMapping
   public ResponseEntity<BaseResponse> updatePost(@RequestBody Post post){
        return new ResponseEntity<>(new BaseResponse(true,postService.updatePost(post)),HttpStatus.OK);
   }

   @GetMapping
   public ResponseEntity<BaseResponse> getAllPost(@RequestParam (value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size",defaultValue = "100")int size,
                                                  @RequestParam(value = "sort",defaultValue = "dateOfModification") String sortBy,
                                                  @RequestParam(value = "topicId",defaultValue = "0") Long topicId,
                                                  @RequestParam(value = "categoryId",defaultValue = "0")Long categoryId,
                                                  @RequestParam(value = "userId",defaultValue = "0")Long userId){
    return new ResponseEntity<>(new BaseResponse(true,postService.getAllPost(page,size,sortBy,topicId,categoryId,userId)),HttpStatus.OK);
   }

}
