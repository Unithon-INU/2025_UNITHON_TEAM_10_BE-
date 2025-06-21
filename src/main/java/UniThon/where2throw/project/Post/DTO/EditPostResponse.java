package UniThon.where2throw.project.Post.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EditPostResponse {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
}
