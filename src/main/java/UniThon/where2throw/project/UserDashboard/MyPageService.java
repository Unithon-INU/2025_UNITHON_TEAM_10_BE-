package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.Global.Exception.CustomException;
import UniThon.where2throw.project.Global.Exception.ErrorCode;
import UniThon.where2throw.project.User.UserRepository;
import UniThon.where2throw.project.User.UserEntity;
import UniThon.where2throw.project.UserDashboard.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository                 userRepo;
    private final UserDashboardRepository        dashRepo;
    private final UserRecycleActivityRepository  activityRepo;
    private final BadgeRepository                badgeRepo;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(String email) {
        // 1) 사용자
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2) 대시보드 요약
        UserDashboardEntity dash = dashRepo.findByUserId(user.getId())
                .orElse(new UserDashboardEntity());

        // 3) 사용자 기본정보 & 레벨
        int level = (int)(dash.getCurrentScore() / 1000) + 1;
        long toNext = Math.max(0, level*1000L - dash.getCurrentScore());
        UserInfoDto userInfo = new UserInfoDto(
                user.getUsername(),
                user.getProfileImageUrl(),
                level,
                dash.getCurrentScore(),
                toNext
        );

        // 4) 요약 탭
        SummaryDto summary = new SummaryDto(
                dash.getTotalRecycle(),
                dash.getBadgeCount(),
                dash.getContinuousDays(),
                dash.getCurrentScore()
        );

        // 5) 통계 탭: 지난 7일치 일별 통계
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);
        List<DailyActivityProjection> raw = activityRepo.findDailyStats(
                user.getId(), weekAgo, today
        );
        List<DailyActivityDto> stats = raw.stream()
                .map(p -> new DailyActivityDto(
                        p.getDate(),
                        p.getRecycleCount(),
                        p.getPointsEarned()
                ))
                .toList();

        // 6) 업적 탭
        List<BadgeDto> earned   = badgeRepo.findEarnedByUser(user.getId());
        List<BadgeDto> unearned = badgeRepo.findUnearnedByUser(user.getId());
        AchievementsDto achievements = new AchievementsDto(earned, unearned);

        return new MyPageResponse(userInfo, summary, stats, achievements);
    }
}
