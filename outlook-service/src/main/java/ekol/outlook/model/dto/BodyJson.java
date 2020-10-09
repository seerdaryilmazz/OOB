package ekol.outlook.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.outlook.model.OutlookMail;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BodyJson {

    private String contentType;
    private String content;

    public OutlookMail.Body toOutlookBody(){
        return OutlookMail.Body.builder()
                .contentType(!StringUtils.isEmpty(getContentType()) ? getContentType() : "Html")
                .content(getContent()).build();
    }
}
