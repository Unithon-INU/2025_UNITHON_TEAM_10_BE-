package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Post.DTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@Tag(name = "게시글 API", description = "게시글 등록·조회·수정·삭제 및 댓글 기능")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/{category}/write")
    @Operation(summary = "게시글 작성", description = "새 게시글을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    public ResponseEntity<CommonResponseDto<CreatePostResponse>> write(
            @Parameter(description = "카테고리 이름", example = "free")
            @PathVariable("category") String category,
            @Parameter(description = "작성할 게시글 DTO")
            @RequestBody CreatePostRequest req,
            Principal principal
    ) {
        String email = principal.getName();
        CreatePostResponse dto = postService.createPost(email, category, req);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @GetMapping("/{category}")
    @Operation(
            summary = "게시글 목록 조회",
            description = "카테고리별 게시글 목록을 조회하고, 키워드 검색·기간 필터·정렬·페이징을 지원합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (결과가 없어도 200)"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터")
    })
    public ResponseEntity<CommonResponseDto<PostListResponse>> list(
            @Parameter(description = "카테고리 이름", example = "notice")
            @PathVariable String category,

            @Parameter(description = "검색 키워드 (제목/내용/작성자)", example = "spring")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "기간 필터: today/week/month/3months/all", example = "month")
            @RequestParam(required = false, defaultValue = "all") String dateRange,

            @Parameter(description = "정렬 기준: latest/views/comments", example = "views")
            @RequestParam(required = false, defaultValue = "latest") String sortBy,

            @Parameter(description = "페이지 번호 (1~)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,

            Principal principal
    ) {
        String email = principal != null ? principal.getName() : null;

        PostListResponse dto = postService.listPosts(
                category, keyword, dateRange, sortBy, page, pageSize, email
        );

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
    @Operation(summary = "게시글 상세 조회", description = "게시글의 내용을 조회하고 조회수를 1 증가시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    public ResponseEntity<CommonResponseDto<PostDetailResponse>> detail(
            @Parameter(description = "카테고리", example = "free")
            @PathVariable String category,
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId,
            Principal principal
    ) {
        String email = principal.getName();
        PostDetailResponse dto = postService.getPostDetail(postId, email);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "작성자만 본문/제목/이미지를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    public ResponseEntity<CommonResponseDto<Object>> edit(
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId,
            @Parameter(description = "수정할 게시글 DTO")
            @RequestBody UpdatePostRequest req
    ) {
        postService.updatePost(postId, req);
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "게시글이 성공적으로 수정되었습니다.")
        ));
    }

    @GetMapping("/{category}/{postId}/edit")
    @Operation(summary = "게시글 수정용 데이터 조회", description = "수정 폼에 기존 내용을 채워 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<CommonResponseDto<EditPostResponse>> getEditPost(
            @Parameter(description = "카테고리", example = "free")
            @PathVariable String category,
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId,
            Principal principal
    ) {
        String email = principal.getName();
        EditPostResponse dto = postService.getPostForEdit(email, postId);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "작성자만 게시글을 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<CommonResponseDto<Object>> remove(
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "게시글이 성공적으로 삭제되었습니다.")
        ));
    }

    // --- 댓글 ---
    @PostMapping("/{category}/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "로그인한 사용자가 댓글을 등록할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    public ResponseEntity<CommonResponseDto<CommentDto>> createComment(
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId,
            @Parameter(description = "댓글 작성 DTO")
            @RequestBody CreateCommentRequest req,
            Principal principal
    ) {
        String email = principal.getName();
        CommentDto dto = commentService.create(email, postId, req);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @DeleteMapping("/{category}/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 작성자만 댓글을 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<CommonResponseDto<Void>> deleteComment(
            @Parameter(description = "게시글 ID", example = "123")
            @PathVariable Long postId,
            @Parameter(description = "댓글 ID", example = "456")
            @PathVariable Long commentId,
            Principal principal
    ) {
        String email = principal.getName();
        commentService.delete(email, commentId);
        return ResponseEntity.ok(CommonResponseDto.success(null));
    }
}
