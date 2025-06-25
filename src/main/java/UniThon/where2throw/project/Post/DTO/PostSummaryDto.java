package UniThon.where2throw.project.Post.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostSummaryDto {
    private Long       id;
    private String     title;
    private String     author;
    private LocalDateTime createdAt;
    private int        viewCnt;
    private boolean    isAuthor;
}