package com.forohub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "respuesta")
public class Response {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    private Boolean solucion = false;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private User author;
}
