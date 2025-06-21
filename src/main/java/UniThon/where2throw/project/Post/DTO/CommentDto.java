package UniThon.where2throw.project.Post.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {
    private Long   id;
    private String author;
    private String content;
    private LocalDateTime createdAt;
}
