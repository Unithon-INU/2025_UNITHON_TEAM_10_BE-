package UniThon.where2throw.project.UserDashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserRecycleActivityRepository
        extends JpaRepository<UserRecycleActivityEntity, Long> {

    /**
     * 지난 기간 동안(예: 7일) 일별 분리배출 건수와 획득 포인트 합계를 조회
     * activity_date의 날짜 부분만 추출해 그룹핑합니다.
     */
    @Query("""
      select
        function('date', ura.activityDate)          as date,
        count(ura)                                   as recycleCount,
        sum(ura.points)                              as pointsEarned
      from UserRecycleActivityEntity ura
      where ura.user.id = :userId
        and function('date', ura.activityDate) between :from and :to
      group by function('date', ura.activityDate)
      order by function('date', ura.activityDate)
      """)
    List<DailyActivityProjection> findDailyStats(
            @Param("userId") Long    userId,
            @Param("from")   LocalDate from,
            @Param("to")     LocalDate to
    );
}
