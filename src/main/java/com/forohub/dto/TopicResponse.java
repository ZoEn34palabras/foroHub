package com.forohub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponse {

    private Long id;
    private String titulo;
    private String mensaje;
    private Instant fechaCreacion;
    private String status;
    private String authorName;
    private String courseName;
}
