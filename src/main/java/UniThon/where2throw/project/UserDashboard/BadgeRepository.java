package UniThon.where2throw.project.UserDashboard;

import UniThon.where2throw.project.UserDashboard.DTO.BadgeDto;
import UniThon.where2throw.project.UserDashboard.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    /**
     * 사용자가 획득한 배지 목록을 DTO 로 바로 조회
     */
    @Query("""
        select new UniThon.where2throw.project.UserDashboard.DTO.BadgeDto(
            b.id, b.name, b.description, b.imageUrl
        )
        from BadgeEntity b
        join b.users u
        where u.id = :userId
    """)
    List<BadgeDto> findEarnedByUser(@Param("userId") Long userId);

    /**
     * 사용자가 획득하지 않은(=아직 못 얻은) 배지 목록을 DTO 로 조회
     */
    @Query("""
        select new UniThon.where2throw.project.UserDashboard.DTO.BadgeDto(
            b.id, b.name, b.description, b.imageUrl
        )
        from BadgeEntity b
        where b.id not in (
            select b2.id from BadgeEntity b2 join b2.users u2 where u2.id = :userId
        )
    """)
    List<BadgeDto> findUnearnedByUser(@Param("userId") Long userId);
}
