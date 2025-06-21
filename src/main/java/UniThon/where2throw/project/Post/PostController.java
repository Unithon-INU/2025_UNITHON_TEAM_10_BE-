package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Post.DTO.CreatePostRequest;
import UniThon.where2throw.project.Post.DTO.CreatePostResponse;
import UniThon.where2throw.project.Post.DTO.PostDetailResponse;
import UniThon.where2throw.project.Post.DTO.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/{category}/write")
    public ResponseEntity<CommonResponseDto<CreatePostResponse>> write(
            @PathVariable("category") String category,
            @RequestBody CreatePostRequest req,
            Principal principal
    ) {
        String email = principal.getName();
        CreatePostResponse dto = postService.createPost(email, category, req);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @GetMapping("/{category}")
    public ResponseEntity<CommonResponseDto<PostListResponse>> list(
            @PathVariable String category,
            @RequestParam(defaultValue = "1")    int page,
            @RequestParam(defaultValue = "10")   int pageSize
    ) {
        PostListResponse dto = postService.listPosts(category, page, pageSize);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @GetMapping("/{category}/{postId}")
    public ResponseEntity<CommonResponseDto<PostDetailResponse>> detail(
            @PathVariable String category,
            @PathVariable Long   postId,
            Principal            principal
    ) {
        String email = principal.getName();
        PostDetailResponse dto =
                postService.getPostDetail(postId, email);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }
}