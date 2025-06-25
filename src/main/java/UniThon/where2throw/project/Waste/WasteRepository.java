package UniThon.where2throw.project.Waste;

import UniThon.where2throw.project.Waste.DTO.WasteSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WasteRepository extends JpaRepository<WasteEntity, String> {
    @Query("select new UniThon.where2throw.project.Waste.DTO.WasteSearchDto(w.id, w.name) " +
            "from WasteEntity w where lower(w.name) like lower(concat('%',:q,'%'))")
    List<WasteSearchDto> searchByName(@Param("q") String query);
}