package UniThon.where2throw.project.User;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.User.DTO.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<CommonResponseDto<ProfileResponse>> getProfile() {
        ProfileResponse dto = profileService.getProfile();
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponseDto<ProfileResponse>> updateProfile(
            @RequestPart("username") String newUsername,
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