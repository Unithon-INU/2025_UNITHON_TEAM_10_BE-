package UniThon.where2throw.project.User;

import UniThon.where2throw.project.UserDashboard.BadgeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    // local, google, apple
    @Column(length = 20, nullable = false)
    private String provider;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(length = 100)
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<BadgeEntity> badges = new HashSet<>();

    @Column(nullable = false)
    private Long points = 0L;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
        this.provider = "local";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
