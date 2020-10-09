package ekol.orders.search.domain;

import javax.persistence.Enumerated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.CompanyType;
import lombok.Data;

@Data
public class HandlingPartySearchDocument {

    @Field(type = FieldType.Long)
    private Long companyId;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String companyName;
    
    @Field(type = FieldType.Long)
    private Long companyLocationId;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String companyLocationName;

    @Field(type = FieldType.Long)
    private Long handlingCompanyId;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String handlingCompanyName;

    @Field(type = FieldType.Long)
    private Long handlingLocationId;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String handlingLocationName;
    
    
    @Enumerated
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private CompanyType handlingCompanyType = CompanyType.COMPANY;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String handlingLocationPostalCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String handlingLocationCountryCode;

    @Field(type = FieldType.Nested)
    private GeoPoint handlingLocationPointOnMap;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String handlingLocationType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String handlingLocationTimezone;

    @Field(type = FieldType.Nested)
    private IdNamePair handlingRegion;

    @Field(type = FieldType.Nested)
    private CodeNamePair handlingRegionCategory;

    @Field(type = FieldType.Nested)
    private IdNamePair handlingOperationRegion;
    
    public int hashCode() {
    	return new HashCodeBuilder(7, 23)
    			.append(companyId)
    			.append(companyLocationId)
    			.append(handlingCompanyId)
    			.append(handlingLocationId)
    			.append(handlingCompanyType)
    			.toHashCode();
    }

    public boolean equals(Object object) {
    	if (!(object instanceof HandlingPartySearchDocument))
            return false;
        if (object == this)
            return true;

        HandlingPartySearchDocument party = (HandlingPartySearchDocument) object;
        return new EqualsBuilder().
                append(companyId, party.getCompanyId()).
                append(companyLocationId, party.getCompanyLocationId()).
                append(handlingCompanyId, party.getHandlingCompanyId()).
                append(handlingLocationId, party.getHandlingLocationId()).
                append(handlingCompanyType, party.getHandlingCompanyType()).
                isEquals();
    }

}
