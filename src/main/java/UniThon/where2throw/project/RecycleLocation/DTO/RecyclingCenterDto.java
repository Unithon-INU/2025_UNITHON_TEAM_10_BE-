package UniThon.where2throw.project.RecycleLocation.DTO;

import java.math.BigDecimal;
import java.util.List;

public record RecyclingCenterDto(
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        List<String> wasteTypes,
        List<String> specialWasteTypes
) {}