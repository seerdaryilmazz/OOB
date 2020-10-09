package ekol.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Created by Dogukan Sahinturk on 26.08.2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ContentTypeJson {
    private String name;
    private String type;
    private String extension;

}
