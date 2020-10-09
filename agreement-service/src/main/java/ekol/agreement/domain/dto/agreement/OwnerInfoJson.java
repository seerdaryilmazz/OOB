package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.ResponsibilityType;
import ekol.agreement.domain.model.OwnerInfo;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OwnerInfoJson {
    private Long id;
    @NotNull(message = "Related People name can not be null")
    private IdNamePair name;
    @NotNull(message = "Related People responsibility type can not be null")
    private ResponsibilityType responsibilityType;
    private List<CodeNamePair> serviceAreas;

    public OwnerInfo toEntity() {
        return OwnerInfo.builder()
                .id(getId())
                .name(getName())
                .responsibilityType(getResponsibilityType())
                .serviceAreas(!CollectionUtils.isEmpty(getServiceAreas()) ? new HashSet<>(getServiceAreas()) : null).build();
    }

    public static OwnerInfoJson fromEntity(OwnerInfo ownerInfo) {
        return new OwnerInfoJsonBuilder()
                .id(ownerInfo.getId())
                .name(ownerInfo.getName())
                .responsibilityType(ownerInfo.getResponsibilityType())
                .serviceAreas(!CollectionUtils.isEmpty(ownerInfo.getServiceAreas()) ? new ArrayList<>(ownerInfo.getServiceAreas()) : null).build();
    }
}
