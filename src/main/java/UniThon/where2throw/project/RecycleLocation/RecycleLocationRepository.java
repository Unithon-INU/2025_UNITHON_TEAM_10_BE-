package UniThon.where2throw.project.RecycleLocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecycleLocationRepository extends JpaRepository<RecycleLocationEntity, Long> {
    @Query(value = """
        SELECT *
          FROM recycle_locations rl
         WHERE (6371 * acos(
                cos(radians(:lat)) 
              * cos(radians(rl.latitude)) 
              * cos(radians(rl.longitude) - radians(:lng))
              + sin(radians(:lat)) * sin(radians(rl.latitude))
         )) <= :radius
         ORDER BY (6371 * acos(
                cos(radians(:lat)) 
              * cos(radians(rl.latitude)) 
              * cos(radians(rl.longitude) - radians(:lng))
              + sin(radians(:lat)) * sin(radians(rl.latitude))
         ))
        """,
            nativeQuery = true
    )
    List<RecycleLocationEntity> findNearby(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radius") double radiusKm
    );
}
