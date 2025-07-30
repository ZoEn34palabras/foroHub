package com.forohub.service;

import com.forohub.dto.TopicRequest;
import com.forohub.dto.TopicResponse;

import java.util.List;

public interface TopicService {
    TopicResponse createTopic(TopicRequest request);
    List<TopicResponse>listAllTopics();
    TopicResponse getTopicById(Long id);
    TopicResponse updateTopic(Long id, TopicRequest request);
    void deleteTopic(Long id);
}
