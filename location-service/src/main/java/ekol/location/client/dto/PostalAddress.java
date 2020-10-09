package ekol.location.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostalAddress implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String streetName;
    private String streetNumber;
    private String doorNumber;
    private String postalCode;
    private String region;
    private String city; //administrative_area_level_1
    private String district; //administrative_area_level_2
    private String locality; //administrative_area_level_3
    private String suburb; //administrative_area_level_4
    private String formattedAddress;
	private Country country;
	private Point pointOnMap;
	private String googlePlaceId;
	private String googlePlaceUrl;
	private String timezone;
}
