package UniThon.where2throw.project.UserDashboard.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "배지 정보 DTO")
public class BadgeDto {
    @Schema(description = "배지 ID",       example = "7")
    private Long   id;

    @Schema(description = "배지 이름",     example = "플라스틱 마스터")
    private String title;

    @Schema(description = "배지 설명",     example = "플라스틱 분리배출 50건 달성")
    private String description;

    @Schema(description = "아이콘 이미지 URL", example = "/assets/badges/plastic_master.png")
    private String iconUrl;
}
