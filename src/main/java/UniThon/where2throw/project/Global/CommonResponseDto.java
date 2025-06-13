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
        return CommonResponseDto.<T>builder()
                .status("SUCCESS")
                .message(null)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String status, String message) {
        return CommonResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}
