package com.forohub.repository;

import com.forohub.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    boolean existsByTituloAndMensaje(String titulo, String mensaje);

}
