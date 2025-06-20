package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Post.DTO.CreatePostRequest;
import UniThon.where2throw.project.Post.DTO.CreatePostResponse;
import UniThon.where2throw.project.Post.PostService;
import jakarta.servlet.http.HttpServletRequest;
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
}