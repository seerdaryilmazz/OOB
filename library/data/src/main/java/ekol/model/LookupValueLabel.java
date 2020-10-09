package ekol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LookupValueLabel {

    private String id;
    private String code;
    private String name;

    public LookupValueLabel() {
    }

    public LookupValueLabel(String id, String name, String code) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
