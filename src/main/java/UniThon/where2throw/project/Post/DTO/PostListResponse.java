package UniThon.where2throw.project.Post.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private List<PostSummaryDto> posts;
    private long totalCount;
    private int totalPages;
    private int currentPage;

}
