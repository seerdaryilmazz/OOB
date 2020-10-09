package ekol.crm.account.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Dogukan Sahinturk on 6.01.2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostalAddress {
    private String streetName;

    private String streetNumber;

    private String doorNumber;

    private String postalCode;

    private Country country;

    private String region;

    private String city; //administrative_area_level_1

    private String district; //administrative_area_level_2

    private String locality; //administrative_area_level_3

    private String suburb; //administrative_area_level_4

    private String formattedAddress;

}
