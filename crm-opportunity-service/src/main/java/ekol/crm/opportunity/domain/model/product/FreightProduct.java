package ekol.crm.opportunity.domain.model.product;


import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.dto.product.FreightProductJson;
import ekol.crm.opportunity.domain.dto.product.ProductJson;
import ekol.crm.opportunity.domain.enumaration.FrequencyType;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FreightProduct extends Product {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreightProduct.class);

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="iso", column=@Column(name="FROM_COUNTRY_ISO")),
            @AttributeOverride(name = "name", column=@Column(name="FROM_COUNTRY_NAME"))})
    private IsoNamePair fromCountry;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="FROM_POINT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="FROM_POINT_NAME"))})
    private IdNamePair fromPoint;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="iso", column=@Column(name="TO_COUNTRY_ISO")),
            @AttributeOverride(name = "name", column=@Column(name="TO_COUNTRY_NAME"))})
    private IsoNamePair toCountry;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="TO_POINT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="TO_POINT_NAME"))})
    private IdNamePair toPoint;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    private Integer frequency;

    @Override
    public ProductJson toJson() {

        FreightProductJson json = null;
        String className = getClass().getSimpleName();
        try {
            json = (FreightProductJson) Class.forName("ekol.crm.opportunity.domain.dto.product."+className+"Json").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("FreightProductJson instantiation error", e);
        }

        json.setId(getId());
        json.setFromCountry(getFromCountry());
        json.setFromPoint(getFromPoint());
        json.setToCountry(getToCountry());
        json.setToPoint(getToPoint());
        json.setExistenceType(getExistenceType());
        json.setFrequencyType(getFrequencyType());
        json.setFrequency(getFrequency());
        json.setExpectedTurnoverPerYear(MonetaryAmountJson.fromEntity(getExpectedTurnoverPerYear()));
        json.setServiceArea(getServiceArea());
        return json;
    }
}
