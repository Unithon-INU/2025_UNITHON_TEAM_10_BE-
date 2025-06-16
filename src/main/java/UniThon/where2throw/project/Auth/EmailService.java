package UniThon.where2throw.project.Auth;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendTemporaryPassword(String email, String tempPassword) throws MessagingException {
        String mailContent = String.format(
                """
                <h3>임시 비밀번호 안내</h3>
                <p>임시 비밀번호는 아래와 같습니다:</p>
                <p><strong>%s</strong></p>
                <p>해당 임시 비밀번호로 로그인한 후, 새로운 비밀번호로 변경해 주세요.</p>
                """, tempPassword
        );

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("hej0681@inu.ac.kr");
        helper.setTo(email);
        helper.setSubject("[Where2Throw] 임시 비밀번호 안내");
        helper.setText(mailContent, true);

        mailSender.send(message);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
    }

}