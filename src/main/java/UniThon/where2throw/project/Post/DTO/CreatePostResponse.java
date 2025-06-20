package UniThon.where2throw.project.Post.DTO;

import lombok.Getter;

@Getter
public class CreatePostResponse {
    private Long postId;
    public CreatePostResponse(Long postId) {
        this.postId = postId;
    }
}