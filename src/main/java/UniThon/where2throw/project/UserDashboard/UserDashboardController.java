package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.UserDashboard.DTO.DashboardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "대시보드 API", description = "홈 화면에 보여줄 통계/요약 정보 제공")
public class UserDashboardController {

    private final UserDashboardService dashboardService;

    @Operation(summary = "내 대시보드 조회",
            description = "현재 로그인된 사용자의 포인트, 분리배출 총 횟수, 연속 활동 일수 등을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponseDto<DashboardDto>> getDashboard(Principal principal) {
        String email = principal.getName();
        DashboardDto dto = dashboardService.getMyDashboard(email);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }
}
