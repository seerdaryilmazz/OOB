package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.VehicleRequirement;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequirementJson {

    private Long id;
    private CodeNamePair requirement;
    private CodeNamePair operationType;
    private boolean removable;

    public VehicleRequirement toEntity(){
        return VehicleRequirement.builder()
                .id(getId())
                .requirement(getRequirement())
                .operationType(getOperationType())
                .removable(isRemovable())
                .build();
    }

    public static VehicleRequirementJson fromEntity(VehicleRequirement vehicleRequirement){
        return new VehicleRequirementJson.VehicleRequirementJsonBuilder()
                .id(vehicleRequirement.getId())
                .requirement(vehicleRequirement.getRequirement())
                .operationType(vehicleRequirement.getOperationType())
                .removable(vehicleRequirement.isRemovable())
                .build();
    }

}
