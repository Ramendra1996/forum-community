package com.forumCommunity.service;

import com.forumCommunity.entity.Options;
import com.forumCommunity.entity.Poll;
import com.forumCommunity.model.BaseResponse;
import com.forumCommunity.model.PollOptions;
import com.forumCommunity.repository.OptionsRepository;
import com.forumCommunity.repository.PollRepository;
import com.forumCommunity.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private OptionsRepository optionsRepository;


    public BaseResponse register(PollOptions pollOptions){
        return new BaseResponse(true,this.create(pollOptions));
    }

    public PollOptions create(PollOptions pollOptions){
        Poll poll = new Poll();
        poll.setDateOfCreation(new Date());
        poll.setDateOfModification(new Date());
        poll.setQuestion(pollOptions.getQuestion());
        poll.setUserId(pollOptions.getUserId());
        Poll savePoll = pollRepository.save(poll);
        List<Options>allOptions = new ArrayList<>();
        pollOptions.getOptions().forEach(e->{
            Options options= new Options();
            options.setDateOfCreation(new Date());
            options.setDateOfModification(new Date());
            options.setPollId(savePoll.getPollId());
            options.setValue(e.getValue());
            allOptions.add(options);
        });
        List<Options> options = optionsRepository.saveAll(allOptions);
        pollOptions.setOptions(options);
        return pollOptions;
    }

    public PollOptions getAllPoll(){
        PollOptions pollOptions = new PollOptions();
        List<Poll> allPoll = pollRepository.findAll();
        List<Options>optionsList = new ArrayList<>();

        allPoll.forEach(poll -> {
             pollOptions.setDateOfCreation(poll.getDateOfCreation());
             pollOptions.setDateOfModification(poll.getDateOfModification());
             pollOptions.setUserId(poll.getUserId());
            pollOptions.setQuestion(poll.getQuestion());
            pollOptions.setPollId(poll.getPollId());
            List<Options> options = optionsRepository.findByPollId(poll.getPollId());
             optionsList.addAll(options);
        });
        pollOptions.setOptions(optionsList);
       return pollOptions;
    }
}
