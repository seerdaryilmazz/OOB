package ekol.json.serializers.common;

/**
 * Created by ozer on 03/11/16.
 */
public enum ConverterType {
    NONE(null), INITIAL_CASE(new InitialCase()), UPPER_CASE(new UpperCase()), HAS_DESCRIPTION(new EnumWithDescriptionConverter());

    private EnumConverter converter;

    private ConverterType(EnumConverter converter) {
        this.converter = converter;
    }

    public String convert(Enum input) {
        return converter != null ? converter.convert(input) : input.name();
    }
}
