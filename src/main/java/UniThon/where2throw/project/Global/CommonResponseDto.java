package UniThon.where2throw.project.Global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {
    private String status;
    private String message;
    private T data;

    public static <T> CommonResponseDto<T> success(T data) {
        return new CommonResponseDto<>("SUCCESS", "요청에 성공했습니다.", data);
    }

    public static <T> CommonResponseDto<T> fail(String message) {
        return new CommonResponseDto<>("FAIL", message, null);
    }

    public static <T> CommonResponseDto<T> fail(String status, String message) {
        return new CommonResponseDto<>(status, message, null);
    }

    public static <T> CommonResponseDto<T> fail(int httpStatusCode, String message) {
        return new CommonResponseDto<>(
                String.valueOf(httpStatusCode),
                message,
                null
        );
    }
}
