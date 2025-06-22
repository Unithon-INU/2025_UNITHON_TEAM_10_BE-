package UniThon.where2throw.project.UserDashboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DashboardDto {
    private Integer totalRecycle;
    private Integer badgeCount;
    private Integer continuousDays;
    private Long currentScore;
    private LocalDateTime updatedAt;
}