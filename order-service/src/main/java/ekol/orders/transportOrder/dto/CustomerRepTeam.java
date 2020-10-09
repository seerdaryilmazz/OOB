package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by kilimci on 30/09/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRepTeam {

    private Long id;
    private String name;
    private List<CustomerRepTeamTag> tags;


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

    public List<CustomerRepTeamTag> getTags() {
        return tags;
    }

    public void setTags(List<CustomerRepTeamTag> tags) {
        this.tags = tags;
    }



}
