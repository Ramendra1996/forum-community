package com.forumCommunity.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ForumCommunityServiceException extends RuntimeException{
    private HttpStatus httpStatus;

    public ForumCommunityServiceException(){

    }
    public ForumCommunityServiceException(String message,HttpStatus httpStatus){
        super(message);
        this.httpStatus=httpStatus;
    }
}
