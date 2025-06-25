package UniThon.where2throw.project.User;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.DTO.ProfileResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final Path uploadDir = Paths.get("uploads/profile");

    private UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public ProfileResponse getProfile() {
        UserEntity user = getCurrentUser();
        return new ProfileResponse(
                user.getUsername(),
                user.getProfileImageUrl()
        );
    }

    @Transactional
    public ProfileResponse updateProfile(String newUsername, MultipartFile image) {
        if (userRepository.existsByUsername(newUsername)) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATED);
        }

        UserEntity u = getCurrentUser();
        u.setUsername(newUsername);

        if (image != null && !image.isEmpty()) {
            try {
                Files.createDirectories(uploadDir);
                String ext = StringUtils.getFilenameExtension(image.getOriginalFilename());
                String filename = UUID.randomUUID() + "." + ext;
                Path target = uploadDir.resolve(filename);
                image.transferTo(target);

                u.setProfileImageUrl("/uploads/profile/" + filename);

            } catch (java.io.IOException ex) {
                throw new CustomException(ErrorCode.FILE_SAVE_FAILED);
            }
        }

        return new ProfileResponse(u.getUsername(), u.getProfileImageUrl());
    }

    @Transactional
    public long addPoints(String email, long points) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        user.setPoints(user.getPoints() + points);
        return user.getPoints();
    }
}
