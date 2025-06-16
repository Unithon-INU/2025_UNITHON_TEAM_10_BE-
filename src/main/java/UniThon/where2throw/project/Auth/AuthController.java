package UniThon.where2throw.project.Auth;

import UniThon.where2throw.project.Auth.DTO.ForgotPasswordRequest;
import UniThon.where2throw.project.Auth.DTO.ResetPasswordRequest;
import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.Global.Security.Jwt.JwtTokenProvider;
import UniThon.where2throw.project.Auth.DTO.LoginRequest;
import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.User.DTO.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증 관련 API", description = "회원가입, 로그인 등")
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
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

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 이용해 로그인을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치 또는 인증 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<Object>> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "로그인이 완료되었습니다. 메인 페이지로 이동합니다.",
                        "token", token)
        ));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자의 JWT 토큰을 블랙리스트 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<Object>> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromHeader(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        jwtTokenProvider.blacklistToken(token);
        return ResponseEntity.ok(CommonResponseDto.success(
                Map.of("message", "로그아웃되었습니다.")
        ));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "비밀번호 찾기", description = "등록된 이메일로 임시 비밀번호를 전송합니다.")
    public ResponseEntity<CommonResponseDto<Object>> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        try {
            authService.sendResetLink(req.email());
            return ResponseEntity.ok(CommonResponseDto.success(Map.of("message", "임시 비밀번호가 이메일로 전송되었습니다.")));
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "로그인된 사용자가 자신의 비밀번호를 변경합니다.")
    public ResponseEntity<CommonResponseDto<Object>> resetPassword(@RequestBody ResetPasswordRequest req, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.getTokenFromHeader(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            String email = jwtTokenProvider.getEmailFromToken(token);

            authService.resetPassword(email, req.getNewPassword());

            return ResponseEntity.ok(CommonResponseDto.success(Map.of("message", "비밀번호가 성공적으로 변경되었습니다.")));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponseDto.fail(e.getMessage()));
        }
    }
}
