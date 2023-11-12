package com.forumCommunity.controller;

import com.forumCommunity.entity.User;
import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserService userService;

    //createUser
    @PostMapping
    public ResponseEntity<BaseResponse> createUser(@RequestBody User user) {
        return new ResponseEntity<>(new BaseResponse(true, userService.createUser(user)), HttpStatus.CREATED);
    }

    //update user
    @PutMapping
    public ResponseEntity<BaseResponse> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(new BaseResponse(true, userService.updateUser(user)), HttpStatus.OK);
    }

    //getAllUser
    @GetMapping
    public ResponseEntity<BaseResponse> getAllUser() {
        return new ResponseEntity<>(new BaseResponse(true, userService.getAllUser()), HttpStatus.OK);
    }

    //getUser by Id
    @GetMapping("{userId}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(new BaseResponse(true, userService.getUserById(userId)), HttpStatus.OK);
    }

    //deleteUser
    @DeleteMapping("{userId}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(new BaseResponse(true, userService.deleteUserById(userId)), HttpStatus.OK);
    }

}
