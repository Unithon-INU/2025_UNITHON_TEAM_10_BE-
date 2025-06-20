package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 5000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PostImage> images = new ArrayList<>();

    @Builder
    public PostEntity(String title, String content, String category, UserEntity author) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
    }

    public void addImage(PostImage img) {
        images.add(img);
        img.setPost(this);
    }
}
