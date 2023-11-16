package com.forumCommunity.controller;

import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.model.PollOptions;
import com.forumCommunity.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/poll")
public class PollController {
    @Autowired
    private PollService pollService;

    @PostMapping
    public ResponseEntity<BaseResponse> createPol(@RequestBody PollOptions pollOptions){
        return new ResponseEntity<>(new BaseResponse(true,pollService.create(pollOptions)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllPoll(){
        return new ResponseEntity<>(new BaseResponse(true,pollService.getAllPoll()),HttpStatus.OK);
    }
}
