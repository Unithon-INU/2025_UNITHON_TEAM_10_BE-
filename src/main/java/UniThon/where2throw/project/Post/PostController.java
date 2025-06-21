package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Post.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<Object>> edit(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest req
    ) {
        postService.updatePost(postId, req);
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "게시글이 성공적으로 수정되었습니다.")
        ));
    }

    @GetMapping("/{category}/{postId}/edit")
    public ResponseEntity<CommonResponseDto<EditPostResponse>> getEditPost(
            @PathVariable String category,
            @PathVariable Long postId,
            Principal principal
    ) {
        String email = principal.getName();
        EditPostResponse dto = postService.getPostForEdit(email, postId);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<Object>> remove(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "게시글이 성공적으로 삭제되었습니다.")
        ));
    }

}