package UniThon.where2throw.project.RecycleLocation;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.RecycleLocation.DTO.RecyclingCenterDto;
import UniThon.where2throw.project.Global.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "재활용 수거함 위치 API", description = "근처 수거함 위치 조회")
public class RecyclingCenterController {

    private final RecyclingCenterService service;

    @GetMapping("/recycling-center")
    @Operation(summary = "근처 분리수거장 조회",
            description = "latitude, longitude, radius(km) 파라미터로 반경 내 수거함을 반환합니다.")
    public ResponseEntity<CommonResponseDto<List<RecyclingCenterDto>>>
    getNearbyCenters(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius
    ) {
        var list = service.findNearby(latitude, longitude, radius);
        if (list.isEmpty()) {
           throw new CustomException(ErrorCode.NOT_FOUND, "해당 지역에 분리수거장이 없습니다.");
        }
        return ResponseEntity.ok(CommonResponseDto.success(list));
    }
}
