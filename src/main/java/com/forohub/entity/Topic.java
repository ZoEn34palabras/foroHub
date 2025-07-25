package com.forohub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topico")
public class Topic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    private String status;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Response> respuestas;
}
