package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.UserDashboard.DTO.MyPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users/me")
@Tag(name = "마이페이지 API", description = "내 활동 요약·통계·업적 조회")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/dashboard")
    @Operation(
            summary     = "내 마이페이지 조회",
            description = "닉네임/레벨/포인트, 요약·통계·업적 탭을 모두 반환합니다."
    )
    public ResponseEntity<CommonResponseDto<MyPageResponse>> getMyPage(Principal principal) {
        String email = principal.getName();
        MyPageResponse dto = myPageService.getMyPage(email);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }
}
