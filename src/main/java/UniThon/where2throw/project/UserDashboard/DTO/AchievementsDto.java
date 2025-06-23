package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "업적 탭: 획득/미획득 배지 리스트 DTO")
public class AchievementsDto {
    @Schema(description = "획득한 배지 목록")
    private List<BadgeDto> earned;

    @Schema(description = "아직 획득하지 않은 배지 목록")
    private List<BadgeDto> unearned;
}
