package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.Post.DTO.CommentDto;
import UniThon.where2throw.project.Post.DTO.CreateCommentRequest;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    @Transactional
    public CommentDto create(String email, Long postId, CreateCommentRequest req) {
        if (!StringUtils.hasText(req.getContent()) || req.getContent().length() > 500) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "댓글은 1~500자여야 합니다.");
        }

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        PostEntity post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        CommentEntity c = new CommentEntity(post, user, req.getContent());
        CommentEntity saved = commentRepo.save(c);

        return new CommentDto(
                saved.getId(),
                saved.getAuthor().getUsername(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    @Transactional
    public void delete(String email, Long commentId) {
        CommentEntity c = commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!c.getAuthor().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        commentRepo.delete(c);
    }
}
