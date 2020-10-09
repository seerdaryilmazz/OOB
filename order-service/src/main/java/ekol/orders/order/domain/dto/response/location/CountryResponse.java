package ekol.orders.order.domain.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryResponse {

    private Long id;

    private String name;

    private String iso;

    private boolean euMember;

    public boolean isTurkey(){
        return "TR".equals(iso);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public boolean isEuMember() {
        return euMember;
    }

    public void setEuMember(boolean euMember) {
        this.euMember = euMember;
    }
}
