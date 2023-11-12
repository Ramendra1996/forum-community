package com.forumCommunity.service;

import com.forumCommunity.entity.User;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //create user
    public User createUser(User user) {
        user.setDateOfCreation(new Date());
        user.setDateOfModification(new Date());
        return userRepository.save(user);
    }

    //update user
    public User updateUser(User user) {
        Optional<User> userObj = userRepository.findById(user.getUserId());
        if (userObj.isEmpty()) {
            throw new ForumCommunityServiceException("User Id Not Valid", HttpStatus.BAD_REQUEST);
        }
        userObj.get().setUserName(user.getUserName());
        userObj.get().setUserDescription(user.getUserDescription());
        userObj.get().setDateOfModification(new Date());
        userObj.get().setUserEmail(user.getUserEmail());
        return userRepository.save(userObj.get());
    }

    // get user by id
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ForumCommunityServiceException("User Id Not valid", HttpStatus.BAD_REQUEST);
        }
        return user.get();
    }

    //get All user
    public List<User> getAllUser() {
        return userRepository.findAll();

    }

    //delete user by Id
    public String deleteUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ForumCommunityServiceException("User Id Not Valid", HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteById(userId);
        return "user delete";
    }
}
