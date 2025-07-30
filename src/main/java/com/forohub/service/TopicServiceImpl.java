package com.forohub.service;

import com.forohub.dto.TopicRequest;
import com.forohub.dto.TopicResponse;
import com.forohub.entity.Course;
import com.forohub.entity.Topic;
import com.forohub.entity.User;
import com.forohub.repository.CourseRepository;
import com.forohub.repository.TopicRepository;
import com.forohub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

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
        if (topicRepository.existsByTituloAndMensaje(req.getTitulo(),req.getMensaje()) {
            throw new DuplicateTopicException("Ya existe un tópico con este título y mensaje")
        }

        User author = userRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new TopicNotFoundException("Autor no encontrado");
        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new TopicNotFoundException("Curso no encontrado");

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

    @Override
    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new TopicNotFoundException("Tópico no existe: " + id);
        }
        topicRepository.deleteById(id);
    }
}
