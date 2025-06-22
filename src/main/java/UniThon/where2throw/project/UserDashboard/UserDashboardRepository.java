package UniThon.where2throw.project.UserDashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserDashboardRepository extends JpaRepository<UserDashboardEntity, Long> {
    Optional<UserDashboardEntity> findByUserId(Long userId);
}
