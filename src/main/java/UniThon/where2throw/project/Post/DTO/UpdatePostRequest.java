package UniThon.where2throw.project.Post.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<String> images;   // (Optional) 이미지 URL 리스트

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setImages(List<String> images) { this.images = images; }
}
