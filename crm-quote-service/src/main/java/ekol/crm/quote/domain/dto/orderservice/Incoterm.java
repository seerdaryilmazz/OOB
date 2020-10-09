package ekol.crm.quote.domain.dto.orderservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Incoterm implements Serializable {

    private Long id;

    private String code;

    private String name;

    private String description;

    private boolean active;

}
