package UniThon.where2throw.project.Auth;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String email, String password) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_INVALID);
        }

        if (!ValidationUtils.isValidPassword(password)) {
            throw new CustomException(ErrorCode.PASSWORD_POLICY_VIOLATION);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        UserEntity user = new UserEntity(email, passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
