package UniThon.where2throw.project.Auth;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$");

    public static boolean isValidEmail(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return PASSWORD_REGEX.matcher(password).matches();
    }
}
