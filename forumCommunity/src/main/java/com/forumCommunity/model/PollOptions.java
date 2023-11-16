package com.forumCommunity.model;

import com.forumCommunity.entity.Options;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PollOptions {
    private Long pollId;
    private Long userId;
    private String question;
    private Date dateOfCreation;
    private Date dateOfModification;

    List<Options>options=new ArrayList<>();
}
