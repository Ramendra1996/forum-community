package com.forumCommunity.service;

import com.forumCommunity.entity.Category;
import com.forumCommunity.entity.Topic;
import com.forumCommunity.exception.ForumCommunityServiceException;
import com.forumCommunity.repository.CategoryRepository;
import com.forumCommunity.repository.PostRepository;
import com.forumCommunity.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;
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
       List<Object[]> topicsWithData = topicRepository.findAllTopicsWithCategoryAndPostCount();
       List<Topic> allTopics = new ArrayList<>();
       for (Object[] result : topicsWithData) {
           Topic topic = (Topic) result[0];
           String categoryName = (String) result[1];
           Long postCount = (Long) result[2];
           topic.setCategoryName(categoryName);
           topic.setPostCount(postCount);
           allTopics.add(topic);
       }
       allTopics.sort(Comparator.comparing(Topic::getTopicName));
       return allTopics;
   }

    public List<Topic> getAllTopicByCategory(Long categoryId) {
        List<Topic> allTopic = topicRepository.findByCategoryId(categoryId);
            List<Object[]> postCountResults = postRepository.countPostByTopicIds(
                    allTopic.stream()
                            .map(Topic::getTopicId)
                            .collect(Collectors.toList())
            );
            Map<Long, Long> postCountMap = postCountResults.stream()
                    .collect(Collectors.toMap(
                            result -> (Long) result[0],
                            result -> (Long) result[1]
                    ));
        Optional<Category> category = categoryRepository.findById(categoryId);
                allTopic.forEach(topic -> {
                    topic.setCategoryName(category.get().getCategoryName());
                    topic.setPostCount(postCountMap.getOrDefault(topic.getTopicId(), 0L));
                });
        return allTopic;
    }

}
//count funcation




   /* public List<Topic>createTopic(List<String> strings){
        List<Topic> topics = new ArrayList<>();
        strings.forEach(m->{
            Topic topic = new Topic();
             topic.setTopicName(m);
             topics.add(topic);
        });
        return  topicRepository.saveAll(topics);
    }*/


