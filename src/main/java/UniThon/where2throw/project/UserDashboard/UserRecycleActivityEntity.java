package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_recycle_activity")
public class UserRecycleActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "activity_date", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "recycle_type")
    private String recycleType;

    @Column(nullable = false)
    private int points;
}
