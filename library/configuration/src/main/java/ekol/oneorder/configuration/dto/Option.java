package ekol.oneorder.configuration.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "valueType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextOption.class, name = "TEXT"),
        @JsonSubTypes.Type(value = NumberOption.class, name = "NUMBER"),
        @JsonSubTypes.Type(value = BooleanOption.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = ListOption.class, name = "LIST"),
        @JsonSubTypes.Type(value = ListOption.class, name = "LOOKUP"),
        @JsonSubTypes.Type(value = RuleOption.class, name = "RULE"),
})
public abstract class Option<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private T value;
}
