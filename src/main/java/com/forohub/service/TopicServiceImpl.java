package com.forohub.service;

import com.forohub.dto.TopicRequest;
import com.forohub.dto.TopicResponse;
import com.forohub.entity.Course;
import com.forohub.entity.Topic;
import com.forohub.entity.User;
import com.forohub.exception.TopicNotFoundException;
import com.forohub.exception.DuplicateTopicException;
import com.forohub.repository.CourseRepository;
import com.forohub.repository.TopicRepository;
import com.forohub.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public TopicResponse createTopic(TopicRequest req) {
        //evitar temas duplicados
        if (topicRepository.existsByTituloAndMensaje(req.getTitulo(), req.getMensaje())) {
            throw new DuplicateTopicException("Ya existe un tópico con este título y mensaje");
        }

        User author = userRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new TopicNotFoundException("Autor no encontrado"));
        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new TopicNotFoundException("Curso no encontrado"));

        Topic topic = new Topic();
        topic.setTitulo(req.getTitulo());
        topic.setMensaje(req.getMensaje());
        topic.setFechaCreacion(Instant.now());
        topic.setStatus("OPEN");
        topic.setAuthor(author);
        topic.setCourse(course);
        Topic saved = topicRepository.save(topic);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> listAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicResponse> listTopics(Pageable pageable) {
        return topicRepository.findAll(pageable)
                .map(this::mapToResponse);
    }


    @Override
    @Transactional(readOnly = true)
    public TopicResponse getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Tópico no existe: " + id));
        return mapToResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponse updateTopic(Long id, TopicRequest req) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Tópico no existe: " + id));
        topic.setTitulo(req.getTitulo());
        topic.setMensaje(req.getMensaje());

        Topic updated = topicRepository.save(topic);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new TopicNotFoundException("Tópico no existe: " + id);
        }
        topicRepository.deleteById(id);
    }
    private TopicResponse mapToResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getTitulo(),
                topic.getMensaje(),
                topic.getFechaCreacion(),
                topic.getStatus(),
                topic.getAuthor().getNombre(),
                topic.getCourse().getNombre()
        );
    }
}
