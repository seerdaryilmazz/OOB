package ekol.crm.opportunity.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.enumaration.FrequencyType;
import ekol.crm.opportunity.domain.model.product.*;
import ekol.exceptions.*;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreightProductJson extends ProductJson  {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreightProductJson.class);

    @NotNull(message = "From country can not be null")
    private IsoNamePair fromCountry;
    private IdNamePair fromPoint;
    @NotNull(message = "To country can not be null")
    private IsoNamePair toCountry;
    private IdNamePair toPoint;
    private FrequencyType frequencyType;
    private Integer frequency;

    public Product toEntity() {
        FreightProduct entity = null;
        try {
            String className = getClass().getSimpleName().split("Json")[0];
            entity = (FreightProduct)Class.forName("ekol.crm.opportunity.domain.model.product." + className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("FreightProduct instantiation error", e);
            throw new BadRequestException("Bad Request", e);
        }

        entity.setId(getId());
        entity.setFromCountry(getFromCountry());
        entity.setFromPoint(getFromPoint());
        entity.setToCountry(getToCountry());
        entity.setToPoint(getToPoint());
        entity.setExistenceType(getExistenceType());
        entity.setFrequencyType(getFrequencyType());
        entity.setFrequency(getFrequency());
        entity.setExpectedTurnoverPerYear(Optional.ofNullable(getExpectedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(null));
        entity.setServiceArea(getServiceArea());
        return entity;
    }
}
