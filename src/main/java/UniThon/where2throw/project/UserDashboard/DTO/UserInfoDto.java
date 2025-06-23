package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 기본정보 및 레벨/포인트 DTO")
public class UserInfoDto {
    @Schema(description = "닉네임", example = "greenHero")
    private String   nickname;

    @Schema(description = "프로필 이미지 URL", example = "/uploads/avatar.png")
    private String   profileImageUrl;

    @Schema(description = "현재 레벨", example = "3")
    private int      level;

    @Schema(description = "현재 포인트", example = "2450")
    private long     currentPoints;

    @Schema(description = "다음 레벨까지 남은 포인트", example = "550")
    private long     pointsToNextLevel;
}
