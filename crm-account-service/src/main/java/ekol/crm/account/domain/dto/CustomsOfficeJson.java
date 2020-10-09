package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.model.CustomsOffice;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomsOfficeJson {

    private Long id;
    private IdNamePair office;

    public CustomsOffice toEntity(){
        return  CustomsOffice.builder()
                .id(getId())
                .office(getOffice())
                .build();
    }

    public static CustomsOfficeJson fromEntity(CustomsOffice customsOffice){
        return new CustomsOfficeJsonBuilder()
                .id(customsOffice.getId())
                .office(customsOffice.getOffice())
                .build();
    }

    public void validate(){

        if(getOffice() == null || getOffice().getId() == null){
            throw new ValidationException("Customs Office should not be empty");
        }
    }
}
