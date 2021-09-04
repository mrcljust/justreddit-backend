package de.justitsolutions.justreddit.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDto {
    private Long id;
    private Long postId;
    private Instant creationDate;
    private String text;
    private String username;
}
