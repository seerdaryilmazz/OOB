package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ozer on 15/03/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {

    private Long externalId;
    private Long id;
    private String type;
    private String name;
    private Long nodeId;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Long getId() {
        return whichId();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNodeId() {
        return whichId();
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    private Long whichId() {
        return id != null ? id : nodeId;
    }
}
