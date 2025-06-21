package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Post.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

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
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PostListResponse dto = postService.listPosts(category, keyword, page, pageSize);

        if (StringUtils.hasText(keyword) && dto.getPosts().isEmpty()) {
            return ResponseEntity.ok(
                    CommonResponseDto.<PostListResponse>builder()
                            .status(String.valueOf(HttpStatus.OK.value()))
                            .message("검색 결과가 없습니다.")
                            .data(dto)
                            .build()
            );
        }

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

    //댓글
    @PostMapping("/{category}/{postId}/comments")
    public ResponseEntity<CommonResponseDto<CommentDto>> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest req,
            Principal principal
    ) {
        String email = principal.getName();
        CommentDto dto = commentService.create(email, postId, req);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @DeleteMapping("/{category}/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Principal principal
    ) {
        String email = principal.getName();
        commentService.delete(email, commentId);
        return ResponseEntity.ok(CommonResponseDto.success(null));
    }
}