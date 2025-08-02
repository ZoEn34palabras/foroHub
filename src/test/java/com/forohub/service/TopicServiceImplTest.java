package com.forohub.service;

import com.forohub.dto.TopicRequest;
import com.forohub.dto.TopicResponse;
import com.forohub.entity.Course;
import com.forohub.entity.Topic;
import com.forohub.entity.User;
import com.forohub.exception.DuplicateTopicException;
import com.forohub.exception.TopicNotFoundException;
import com.forohub.repository.CourseRepository;
import com.forohub.repository.TopicRepository;
import com.forohub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceImplTest {

    @InjectMocks
    private TopicServiceImpl topicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTopic_success() {
        // Datos de entrada
        TopicRequest req = new TopicRequest(
                "Título test",
                "Mensaje test",
                1L, 1L
        );

        // Mocks de entidades relacionadas
        User user = new User();
        user.setId(1L);
        user.setNombre("Usuario Test");

        Course course = new Course();
        course.setId(1L);
        course.setNombre("Curso Test");

        Topic savedTopic = new Topic();
        savedTopic.setId(10L);
        savedTopic.setTitulo(req.getTitulo());
        savedTopic.setMensaje(req.getMensaje());
        savedTopic.setAuthor(user);
        savedTopic.setCourse(course);

        // Mockeo de comportamiento
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(topicRepository.existsByTituloAndMensaje(req.getTitulo(), req.getMensaje())).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(savedTopic);

        // Ejecutar
        TopicResponse result = topicService.createTopic(req);

        // Verificar
        assertEquals("Título test", result.getTitulo());
        assertEquals("Curso Test", result.getCourseName());
        assertEquals("Usuario Test", result.getAuthorName());
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void testCreateTopic_failsIfDuplicate() {
        TopicRequest req = new TopicRequest(
                "Título duplicado",
                "Mensaje duplicado",
                1L, 1L
        );

        when(topicRepository.existsByTituloAndMensaje(req.getTitulo(), req.getMensaje()))
                .thenReturn(true);

        DuplicateTopicException ex = assertThrows(DuplicateTopicException.class, () -> {
            topicService.createTopic(req);
        });

        assertEquals("Ya existe un tópico con este título y mensaje", ex.getMessage());
        verify(topicRepository, never()).save(any());
    }

    @Test
    void testCreateTopic_failsIfUserNotFound() {
        TopicRequest req = new TopicRequest(
                "Sin autor",
                "Mensaje test",
                99L, 1L
        );

        when(topicRepository.existsByTituloAndMensaje(req.getTitulo(), req.getMensaje()))
                .thenReturn(false);
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        TopicNotFoundException ex = assertThrows(TopicNotFoundException.class, () -> {
            topicService.createTopic(req);
        });

        assertEquals("Autor no encontrado", ex.getMessage());
    }

    @Test
    void testCreateTopic_failsIfCourseNotFound() {
        TopicRequest req = new TopicRequest(
                "Sin curso",
                "Mensaje test",
                1L, 99L
        );

        User user = new User();
        user.setId(1L);
        user.setNombre("Juan");

        when(topicRepository.existsByTituloAndMensaje(req.getTitulo(), req.getMensaje()))
                .thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        TopicNotFoundException ex = assertThrows(TopicNotFoundException.class, () -> {
            topicService.createTopic(req);
        });

        assertEquals("Curso no encontrado", ex.getMessage());
    }

}
