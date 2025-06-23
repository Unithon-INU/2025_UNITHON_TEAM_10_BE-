package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "내 마이페이지 전체 응답 DTO")
public class MyPageResponse {
    @Schema(description = "사용자 기본정보")
    private UserInfoDto       userInfo;

    @Schema(description = "요약 탭 정보")
    private SummaryDto        summary;

    @Schema(description = "통계 탭: 지난 7일 일별 활동 데이터")
    private List<DailyActivityDto> stats;

    @Schema(description = "업적 탭: 획득/미획득 배지 목록")
    private AchievementsDto   achievements;
}
