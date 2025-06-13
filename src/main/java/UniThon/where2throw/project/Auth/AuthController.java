package UniThon.where2throw.project.Auth;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.User.DTO.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증 관련 API", description = "회원가입, 로그인 등")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "회원가입", description = "이메일과 비밀번호를 이용해 신규 회원을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "409", description = "중복된 이메일")
    })
    @PostMapping("/register")
    public ResponseEntity<CommonResponseDto<Object>> register(@RequestBody RegisterRequest req) {
        authService.register(req.email(), req.password());
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.")
        ));
    }
}
