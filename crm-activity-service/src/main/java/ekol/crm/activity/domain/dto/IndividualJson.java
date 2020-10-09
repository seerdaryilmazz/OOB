package ekol.crm.activity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.domain.Individual;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IndividualJson {
    private Long id;
    private String name;
    private String username;
    private String emailAddress;

    public Individual toEntity(){
        return Individual.builder()
                .id(getId())
                .name(getName())
                .username(getUsername())
                .emailAddress(getEmailAddress()).build();
    }

    public static IndividualJson fromEntity(Individual individual){
        return new IndividualJson.IndividualJsonBuilder()
                .id(individual.getId())
                .name(individual.getName())
                .username(individual.getUsername())
                .emailAddress(individual.getEmailAddress()).build();
    }

}
