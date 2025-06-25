package UniThon.where2throw.project.RecycleLocation.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecyclingCenterDto {
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String specialWasteType;
}
