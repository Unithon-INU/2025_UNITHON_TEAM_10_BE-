package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.User.UserRepository;
import UniThon.where2throw.project.UserDashboard.DTO.DashboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDashboardService {
    private final UserDashboardRepository dashboardRepo;
    private final UserRepository userRepo;

    @Transactional
    public DashboardDto getMyDashboard(String email) {
        UserEntity me = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserDashboardEntity dash = dashboardRepo
                .findByUserId(me.getId())
                .orElseGet(() -> createDefaultDashboard(me));

        return new DashboardDto(
                dash.getTotalRecycle(),
                dash.getBadgeCount(),
                dash.getContinuousDays(),
                dash.getCurrentScore(),
                dash.getUpdatedAt()
        );
    }

    private UserDashboardEntity createDefaultDashboard(UserEntity user) {
        UserDashboardEntity e = new UserDashboardEntity(
                user,
                0,
                0,
                0,
                0L
        );
        return dashboardRepo.save(e);
    }
}
