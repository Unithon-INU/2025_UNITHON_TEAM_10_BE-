package UniThon.where2throw.project.User;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.User.DTO.ProfileResponse;
import UniThon.where2throw.project.User.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/me")
@Tag(name = "프로필 API", description = "내 프로필 조회·수정")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "내 프로필 조회", description = "로그인된 사용자의 프로필 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<CommonResponseDto<ProfileResponse>> getProfile() {
        ProfileResponse dto = profileService.getProfile();
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "내 프로필 수정", description = "닉네임과 프로필 이미지를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    public ResponseEntity<CommonResponseDto<ProfileResponse>> updateProfile(
            @Parameter(description = "새 닉네임", example = "newNick")
            @RequestPart("username") String newUsername,

            @Parameter(description = "새 프로필 이미지 파일")
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            ProfileResponse dto = profileService.updateProfile(newUsername, image);
            return ResponseEntity.ok(CommonResponseDto.success(dto));
        } catch (CustomException e) {
            return ResponseEntity
                    .status(e.getErrorCode().getStatus())
                    .body(CommonResponseDto.fail(e.getErrorCode().name(), e.getMessage()));
        }
    }
}
