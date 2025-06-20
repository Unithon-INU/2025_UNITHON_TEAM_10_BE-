package UniThon.where2throw.project.Post.DTO;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreatePostRequest {
    private String title;
    private String content;
    private List<String> images;
}