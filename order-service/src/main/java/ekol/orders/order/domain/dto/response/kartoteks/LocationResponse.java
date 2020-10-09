package ekol.orders.order.domain.dto.response.kartoteks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {

    private Long id ;

    private String name;

    private String shortName;

    private String timezone;

    private PostalAddress postaladdress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public PostalAddress getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(PostalAddress postaladdress) {
        this.postaladdress = postaladdress;
    }
}
