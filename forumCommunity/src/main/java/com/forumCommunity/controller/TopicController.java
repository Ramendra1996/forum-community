package com.forumCommunity.controller;

import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.service.CategoryService;
import com.forumCommunity.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private CategoryService categoryService;

    //create topic
    @PostMapping
    public ResponseEntity<BaseResponse> createTopic(@RequestBody Topic topic) {
        return new ResponseEntity<>(new BaseResponse(true, topicService.createTopic(topic)), HttpStatus.CREATED);
    }

    //update topic
    @PutMapping
    public ResponseEntity<BaseResponse> updateTopic(@RequestBody Topic topic) {
        return new ResponseEntity<>(new BaseResponse(true, updateTopic(topic)), HttpStatus.OK);
    }

    //getAll topic
    @GetMapping
    public ResponseEntity<BaseResponse> getAllTopic() {
        return new ResponseEntity<>(new BaseResponse(true, topicService.getAllTopic()), HttpStatus.OK);
    }
    @GetMapping("{categoryId}")
     public ResponseEntity<BaseResponse> getAllTopicByCategory(@PathVariable("categoryId")Long categoryId){
        return  new ResponseEntity<>(new BaseResponse(true,topicService.getAllTopicByCategory(categoryId)),HttpStatus.OK);
     }


   /* @PostMapping
    public ResponseEntity<List<Topic>>createTopic(@RequestBody List<String>topics){
       return new  ResponseEntity<>(topicService.createTopic(topics), HttpStatus.OK);
   }*/


    //category controller

    //create category
    @PostMapping("/category")
    public  ResponseEntity<BaseResponse> createCategory(@RequestBody Category category){
        return  new ResponseEntity<>(new BaseResponse(true,categoryService.createCategory(category)),HttpStatus.CREATED);

    }

    //update category
    @PutMapping("/category")
    public  ResponseEntity<BaseResponse> updateCategory(@RequestBody Category category){
        return new ResponseEntity<>(new BaseResponse(true,categoryService.updateCategory(category)),HttpStatus.OK);
    }

    //get All topic
    @GetMapping("/category")
    public  ResponseEntity<BaseResponse> getAllCategory(){
        return new ResponseEntity<>(new BaseResponse(true,categoryService.getAllCategory()),HttpStatus.OK);
    }


}
