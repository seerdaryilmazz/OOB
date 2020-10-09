package ekol.crm.quote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class EmailJson {

    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
}
