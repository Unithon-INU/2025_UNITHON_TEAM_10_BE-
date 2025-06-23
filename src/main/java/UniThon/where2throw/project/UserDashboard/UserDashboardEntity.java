package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_dashboard")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "total_recycle",    nullable = false) private int   totalRecycle;
    @Column(name = "badge_count",      nullable = false) private int   badgeCount;
    @Column(name = "continuous_days",  nullable = false) private int   continuousDays;
    @Column(name = "current_score",    nullable = false) private long  currentScore;

    @Column(name = "updated_at",       nullable = false)
    private LocalDateTime updatedAt;
}
