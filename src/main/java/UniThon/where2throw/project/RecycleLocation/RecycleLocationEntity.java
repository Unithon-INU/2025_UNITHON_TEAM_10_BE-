package UniThon.where2throw.project.RecycleLocation;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recycle_locations")
public class RecycleLocationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String district;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Column(name = "source_id")
    private String sourceId;
    private String specialWasteType;
}
