package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Post.DTO.*;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;

    @Transactional
    public CreatePostResponse createPost(
            String email,
            String category,
            CreatePostRequest req
    ) {
        if (StringUtils.isEmpty(req.getTitle()) || req.getTitle().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "제목은 1~100자여야 합니다.");
        }
        if (StringUtils.isEmpty(req.getContent()) || req.getContent().length() > 5000) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "내용은 1~5000자여야 합니다.");
        }
        if (req.getImages() != null && req.getImages().size() > 5) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "이미지는 최대 5개까지 업로드 가능합니다.");
        }

        UserEntity author = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        PostEntity post = PostEntity.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .category(category)
                .author(author)
                .build();

        if (req.getImages() != null) {
            for (String imgUrl : req.getImages()) {
                if (StringUtils.isEmpty(imgUrl)) continue;
                PostImage img = new PostImage(imgUrl);
                post.addImage(img);
            }
        }

        post = postRepo.save(post);
        return new CreatePostResponse(post.getId());
    }

    @Transactional(readOnly = true)
    public PostListResponse listPosts(String category, int page, int pageSize) {
        // (1) 페이지 요청 (0-base) + 최신순 정렬
        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // (2) DB 조회
        Page<PostEntity> p = postRepo.findByCategory(category, pageable);

        // (3) DTO 매핑: 제목/작성자/작성일/조회수만
        List<PostSummaryDto> list = p.getContent().stream()
                .map(post -> new PostSummaryDto(
                        post.getId(),
                        post.getTitle(),
                        post.getAuthor().getUsername(),
                        post.getCreatedAt(),
                        post.getViewCount()
                ))
                .toList();

        // (4) 페이지 정보 담아서 반환
        return new PostListResponse(
                list,
                p.getTotalElements(),
                p.getTotalPages(),
                p.getNumber() + 1
        );
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long postId, String currentEmail) {
        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "게시글이 없습니다."));

        post.incrementView();

        List<String> imgs = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .toList();

        List<CommentDto> comments = commentRepo.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getAuthor().getUsername(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();

        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getViewCount(),
                imgs,
                comments
        );
    }
}
