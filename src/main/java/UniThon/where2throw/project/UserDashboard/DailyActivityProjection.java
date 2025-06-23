package UniThon.where2throw.project.UserDashboard;

import java.time.LocalDate;

public interface DailyActivityProjection {
    LocalDate getDate();
    long      getRecycleCount();
    long      getPointsEarned();
}
