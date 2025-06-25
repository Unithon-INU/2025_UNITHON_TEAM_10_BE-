package UniThon.where2throw.project.Waste;

import UniThon.where2throw.project.Global.CommonResponseDto;
import UniThon.where2throw.project.Waste.DTO.WasteDto;
import UniThon.where2throw.project.Waste.DTO.WasteSearchDto;
import UniThon.where2throw.project.Waste.DTO.ClassificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/waste")
@Tag(name = "폐기물 API", description = "폐기물 조회·검색·분류 기능을 제공합니다.")
@RequiredArgsConstructor
public class WasteController {

    private final WasteService wasteService;

    @GetMapping("/{id}")
    @Operation(summary = "폐기물 상세 조회", description = "주어진 ID로 폐기물(쓰레기) 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 ID 형식"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 폐기물을 찾을 수 없음")
    })
    public ResponseEntity<CommonResponseDto<WasteDto>> getWaste(
            @Parameter(description = "조회할 폐기물 ID", example = "placstic")
            @PathVariable String id
    ) {
        WasteDto dto = wasteService.getById(id);
        return ResponseEntity.ok(CommonResponseDto.success(dto));
    }

    @GetMapping("/search")
    @Operation(summary = "폐기물 검색", description = "키워드를 통해 폐기물 정보를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공 (결과가 없어도 200)"),
            @ApiResponse(responseCode = "400", description = "검색어가 없습니다.")
    })
    public ResponseEntity<CommonResponseDto<List<WasteSearchDto>>> search(
            @Parameter(description = "검색할 키워드 (이름, 유형 등)", example = "페트병")
            @RequestParam String query
    ) {
        List<WasteSearchDto> list = wasteService.search(query);
        return ResponseEntity.ok(CommonResponseDto.success(list));
    }

    @PostMapping("/{type}")
    @Operation(summary = "재활용 처리", description = "재활용 품목 타입과 개수를 넘기면, 개수×5점만큼 포인트를 적립하고 현재 점수를 반환합니다.")
    public ResponseEntity<CommonResponseDto<Long>> recycle(
            @Parameter(description = "재활용 품목 타입", example = "plastic")
            @PathVariable String type,
            @Parameter(description = "재활용한 개수", example = "3")
            @RequestParam int quantity,
            Principal principal
    ) {
        String email = principal.getName();
        long currentScore = wasteService.recycle(email, quantity);
        return ResponseEntity.ok(CommonResponseDto.success(currentScore));
    }
}
