package UniThon.where2throw.project.Waste.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WasteDto {
    private String id;
    private String name;
    private String disposalMethod;
    private String createdByAi;
}
