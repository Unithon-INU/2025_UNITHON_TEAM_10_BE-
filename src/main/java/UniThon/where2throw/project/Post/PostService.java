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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
    public PostListResponse listPosts(
            String category,
            String keyword,
            String dateRange,
            String sortBy,
            int page,
            int pageSize
    ) {
        // 1) 날짜 필터 스펙
        Specification<PostEntity> spec = Specification
                .where(categoryEquals(category))
                .and(keyword != null && !keyword.isBlank() ? keywordContains(keyword) : null)
                .and(dateRangeFilter(dateRange));

        // 2) 정렬
        Sort sort = switch (sortBy) {
            case "views"    -> Sort.by(Sort.Direction.DESC, "viewCount");
            case "comments" -> Sort.by(Sort.Direction.DESC, "comments.size"); // 댓글 컬렉션 사이즈로 정렬
            default         -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        // 3) 페이징 + Specification 조회
        Page<PostEntity> p = postRepo.findAll(spec, pageable);

        // 4) DTO 변환
        List<PostSummaryDto> list = p.getContent().stream().map(post -> {
            boolean isAuthor = false; // 필요 시 비교로 세팅
            return new PostSummaryDto(
                    post.getId(),
                    post.getTitle(),
                    post.getAuthor().getUsername(),
                    post.getCreatedAt(),
                    post.getViewCount()
            );
        }).toList();

        return new PostListResponse(
                list,
                p.getTotalElements(),
                p.getTotalPages(),
                p.getNumber() + 1
        );
    }

    private Specification<PostEntity> categoryEquals(String category) {
        return (root, cq, cb) -> cb.equal(root.get("category"), category);
    }

    private Specification<PostEntity> keywordContains(String kw) {
        return (root, cq, cb) -> {
            String pattern = "%" + kw.trim() + "%";
            return cb.or(
                    cb.like(root.get("title"), pattern),
                    cb.like(root.get("content"), pattern),
                    cb.like(root.get("author").get("username"), pattern)
            );
        };
    }

    private Specification<PostEntity> dateRangeFilter(String range) {
        return (root, cq, cb) -> {
            LocalDateTime from;
            switch (range) {
                case "today"    -> from = LocalDateTime.now().toLocalDate().atStartOfDay();
                case "week"     -> from = LocalDateTime.now().minusWeeks(1);
                case "month"    -> from = LocalDateTime.now().minusMonths(1);
                case "3months"  -> from = LocalDateTime.now().minusMonths(3);
                default         -> from = LocalDateTime.of(1970,1,1,0,0);
            }
            return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
        };
    }

    private String abbreviate(String content) {
        if (content == null) return "";
        return content.length() <= 50
                ? content
                : content.substring(0, 50) + "...";
    }
    @Transactional
    public PostDetailResponse getPostDetail(Long postId, String currentEmail) {
        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "게시글이 없습니다."));

        post.incrementView();

        List<String> imgs = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .toList();

        List<CommentDto> comments = commentRepo.findByPostIdOrderByCreatedAtDesc(postId).stream()
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

    @Transactional
    public void updatePost(Long postId, UpdatePostRequest req) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity me = userRepo.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!post.getAuthor().getId().equals(me.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403
        }

        if (StringUtils.isEmpty(req.getTitle()) || req.getTitle().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "제목은 1~100자여야 합니다.");
        }
        if (StringUtils.isEmpty(req.getContent()) || req.getContent().length() > 5000) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "내용은 1~5000자여야 합니다.");
        }
        if (req.getImages() != null && req.getImages().size() > 5) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "이미지는 최대 5개까지 업로드 가능합니다.");
        }

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());

        post.getImages().clear();
        if (req.getImages() != null) {
            for (String url : req.getImages()) {
                if (StringUtils.isEmpty(url)) continue;
                PostImage img = new PostImage(url);
                post.addImage(img);
            }
        }
    }

    @Transactional(readOnly = true)
    public EditPostResponse getPostForEdit(String email, Long postId) {
        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<String> urls = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .toList();

        return new EditPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                urls
        );
    }

    @Transactional
    public void deletePost(Long postId) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity me = userRepo.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!post.getAuthor().getId().equals(me.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403
        }

        postRepo.delete(post);
    }
}
