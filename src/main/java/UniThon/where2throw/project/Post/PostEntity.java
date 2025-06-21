package UniThon.where2throw.project.Post;

import UniThon.where2throw.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "posts")
public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String content;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    private int viewCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public PostEntity(String title,
                      String content,
                      String category,
                      UserEntity author) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
    }

    public void addImage(PostImage img) {
        images.add(img);
        img.setPost(this);
    }

    public void incrementView() {
        this.viewCount++;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
