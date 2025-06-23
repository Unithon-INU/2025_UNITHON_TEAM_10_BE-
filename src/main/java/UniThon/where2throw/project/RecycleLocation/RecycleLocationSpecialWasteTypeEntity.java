package UniThon.where2throw.project.RecycleLocation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recycle_location_special_waste_types")
public class RecycleLocationSpecialWasteTypeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "special_waste_type")
    private String specialWasteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private RecycleLocationEntity location;
}
