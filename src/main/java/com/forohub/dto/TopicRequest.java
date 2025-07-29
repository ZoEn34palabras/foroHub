package com.forohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicRequest {

    @NotBlank
    @Size(max = 255)
    private String titulo;

    @NotBlank
    private String mensaje;

    @NotNull
    private Long authorId;

    @NotNull
    private Long courseId;
}
