package UniThon.where2throw.project.Waste;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.ProfileService;
import UniThon.where2throw.project.UserDashboard.MyPageService;
import UniThon.where2throw.project.Waste.DTO.RecycleResultDto;
import UniThon.where2throw.project.Waste.DTO.WasteDto;
import UniThon.where2throw.project.Waste.DTO.WasteSearchDto;
import UniThon.where2throw.project.Waste.DTO.ClassificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WasteService {
    private final WasteRepository wasteRepo;
    private final MyPageService myPageService;
    private final ProfileService profileService;

    public WasteDto getById(String id) {
        WasteEntity w = wasteRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        return new WasteDto(
                w.getId(), w.getName(), w.getDisposalMethod(), w.getCreatedByAi()
        );
    }

    public List<WasteSearchDto> search(String query) {
        return wasteRepo.searchByName(query);
    }

    @Transactional
    public RecycleResultDto recycle(String email, String type, int quantity) {
        long pointsToAdd = (long) quantity * 5;
        WasteDto w = classify(type);

        return new RecycleResultDto(pointsToAdd, w);
    }

    @Transactional
    public WasteDto classify(String wasteId) {
        WasteEntity w = wasteRepo.findById(wasteId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return new WasteDto(
                w.getId(), w.getName(), w.getDisposalMethod(), w.getCreatedByAi()
        );
    }


    @Transactional
    public WasteDto correctWasteInfo(String id, ClassificationRequest req) {
        WasteEntity w = wasteRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        w.changeName(req.getName());
        w.changeDisposalMethod(req.getDisposalMethod());
        w.markCreatedByAi();
        wasteRepo.save(w);
        return new WasteDto(
                w.getId(), w.getName(), w.getDisposalMethod(), w.getCreatedByAi()
        );
    }
}

