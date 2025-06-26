package UniThon.where2throw.project.Auth;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.Global.Security.Jwt.JwtTokenProvider;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EmailService emailService;

    public void register(String email, String password, String username) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_INVALID);
        }

        if (!ValidationUtils.isValidPassword(password)) {
            throw new CustomException(ErrorCode.PASSWORD_POLICY_VIOLATION);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATED);
        }

        String encryptedPassword = passwordEncoder.encode(password);
        UserEntity user = new UserEntity(email, encryptedPassword, username);
        userRepository.save(user);
    }

    public String login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getEmail());
    }
    public void sendResetLink(String email) throws MessagingException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        emailService.sendTemporaryPassword(email, tempPassword);

        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
    }

    public void resetPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_POLICY_VIOLATION);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}