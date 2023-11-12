package com.forumCommunity.exception;

import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ForumCommunityExceptionHandler {

    @ExceptionHandler(ForumCommunityServiceException.class)
    public ResponseEntity<BaseResponse> handleScreenerServiceException(WebRequest req, ForumCommunityServiceException ex){
        ErrorResponse err=new ErrorResponse(LocalDateTime.now(),ex.getMessage(),req.getDescription(false));
        return new ResponseEntity<>(new BaseResponse(false,err),ex.getHttpStatus());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> accessDeniedExceptionHandleException(WebRequest req,AccessDeniedException ex){
        ErrorResponse err=new ErrorResponse(LocalDateTime.now(),ex.getMessage(),req.getDescription(false));
        return new ResponseEntity<>(new BaseResponse(false,err), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(WebRequest req, Exception ex){
        ErrorResponse err=new ErrorResponse(LocalDateTime.now(),ex.getMessage(),req.getDescription(false));
        return new ResponseEntity<>(new BaseResponse(false,err), HttpStatus.BAD_REQUEST);
    }

}
