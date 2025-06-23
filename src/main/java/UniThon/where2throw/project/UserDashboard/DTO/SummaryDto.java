package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "요약 탭 핵심 지표 DTO")
public class SummaryDto {
    @Schema(description = "총 분리배출 건수", example = "42")
    private int   totalRecycleCount;

    @Schema(description = "획득 배지 수", example = "5")
    private int   badgeCount;

    @Schema(description = "연속 활동 일수", example = "12")
    private int   continuousDays;

    @Schema(description = "현재 포인트", example = "2450")
    private long  currentPoints;
}
