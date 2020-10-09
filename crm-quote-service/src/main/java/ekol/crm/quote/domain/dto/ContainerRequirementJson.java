package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.ContainerRequirement;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContainerRequirementJson {

    private Long id;
    private CodeNamePair volume;
    private CodeNamePair type;
    private Integer quantity;

    public ContainerRequirement toEntity(){
        return ContainerRequirement.builder()
                .id(getId())
                .volume(getVolume())
                .type(getType())
                .quantity(getQuantity())
                .build();
    }

    public static ContainerRequirementJson fromEntity(ContainerRequirement containerRequirement){
        return new ContainerRequirementJsonBuilder()
                .id(containerRequirement.getId())
                .volume(containerRequirement.getVolume())
                .type(containerRequirement.getType())
                .quantity(containerRequirement.getQuantity())
                .build();
    }

}
