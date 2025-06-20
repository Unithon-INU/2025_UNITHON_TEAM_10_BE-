package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Post.DTO.CreatePostRequest;
import UniThon.where2throw.project.Post.DTO.CreatePostResponse;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;

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
}
