package UniThon.where2throw.project.Waste;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "waste")
public class WasteEntity {
    @Id
    @Column(nullable = false, length = 100)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "disposal_method", length = 500)
    private String disposalMethod;

    @Column(name = "created_by_ai", nullable = false, length = 1)
    private String createdByAi = "N";

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public void markCreatedByAi() {
        this.createdByAi = "Y";
    }
}
