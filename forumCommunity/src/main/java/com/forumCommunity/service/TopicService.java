package com.forumCommunity.service;

import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.repository.CategoryRepository;
import com.forumCommunity.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;
//create topic
    public Topic createTopic(Topic topic){
        topic.setDateOfCreation(new Date());
        topic.setDateOfModification(new Date());
        return  topicRepository.save(topic);
    }
// update topic
    public  Topic updateTopic(Topic topic){
        Optional<Topic> topicObj = topicRepository.findById(topic.getTopicId());
        if(topicObj.isEmpty()){
            throw  new ForumCommunityServiceException("Topic Id Not Valid", HttpStatus.BAD_REQUEST);
        }
        topicObj.get().setTopicName(topic.getTopicName());
        topicObj.get().setDateOfModification(new Date());
        return  topicRepository.save(topicObj.get());
    }
//get All topic
   public List<Topic> getAllTopic(){
       List<Topic> allTopic = topicRepository.findAll();
       allTopic.forEach(topic->{
           Optional<Category> category = categoryRepository.findById(topic.getCategoryId());
           topic.setCategoryName(category.get().getCategoryName());
           topic.setPostCount(findAllTopicsWithPostCount().stream().count());

       });
       return allTopic;
   }
//count funcation
    public List<Topic> findAllTopicsWithPostCount() {
        List<Object[]> results = topicRepository.findAllTopicsWithPostCount();
        List<Topic> usersWithPostCounts = new ArrayList<>();
        for (Object[] result : results) {
            Topic topic = (Topic) result[0];
            Long postCount = (Long) result[1];
            topic.setPostCount(postCount);
            usersWithPostCounts.add(topic);
        }
        return usersWithPostCounts;
    }



   /* public List<Topic>createTopic(List<String> strings){
        List<Topic> topics = new ArrayList<>();
        strings.forEach(m->{
            Topic topic = new Topic();
             topic.setTopicName(m);
             topics.add(topic);
        });
        return  topicRepository.saveAll(topics);
    }*/

}
