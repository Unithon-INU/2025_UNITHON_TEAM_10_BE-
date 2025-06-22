package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_dashboard")
@Getter
@NoArgsConstructor
public class UserDashboardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    private int totalRecycle;

    @Column(nullable = false)
    private int badgeCount;

    @Column(nullable = false)
    private int continuousDays;

    @Column(nullable = false)
    private long currentScore;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserDashboardEntity(
            UserEntity user,
            int totalRecycle,
            int badgeCount,
            int continuousDays,
            long currentScore
    ) {
        this.user = user;
        this.totalRecycle = totalRecycle;
        this.badgeCount = badgeCount;
        this.continuousDays = continuousDays;
        this.currentScore = currentScore;
        // updatedAt는 콜백에서 채워집니다.
    }

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
