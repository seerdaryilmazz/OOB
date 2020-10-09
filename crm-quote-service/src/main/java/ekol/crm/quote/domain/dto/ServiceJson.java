package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceJson {

    private Long id;
    private ServiceType type;

    public Service toEntity(){
        return Service.builder()
                .id(getId())
                .type(getType()).build();
    }

    public static ServiceJson fromEntity(Service service){
        return new ServiceJsonBuilder()
                .id(service.getId())
                .type(service.getType()).build();
    }

}
