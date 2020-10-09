package ekol.orders.transportOrder.domain;

import java.io.Serializable;

/**
 * Created by kilimci on 09/09/16.
 */
public class CompanySegmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
