package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "통계 탭 일별 활동 데이터 DTO")
public class DailyActivityDto {
    @Schema(description = "날짜",        example = "2025-06-22")
    private LocalDate date;

    @Schema(description = "당일 분리배출 건수", example = "3")
    private long      recycleCount;

    @Schema(description = "당일 획득 포인트", example = "150")
    private long      pointsEarned;
}
