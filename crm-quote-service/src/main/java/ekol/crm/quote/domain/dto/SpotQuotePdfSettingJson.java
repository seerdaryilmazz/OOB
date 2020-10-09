package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.model.SpotQuotePdfSetting;
import ekol.crm.quote.domain.model.SpotQuotePdfSettingForListing;
import ekol.crm.quote.util.BeanUtils;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpotQuotePdfSettingJson {

    private Long id;

    private IdNamePair subsidiary;

    private CodeNamePair serviceArea;

    private SupportedLocale language;

    private String aboutCompany;

    private String importGeneralConditions;

    private String exportGeneralConditions;

    public SpotQuotePdfSetting toEntity() {
        return SpotQuotePdfSetting.builder()
                .id(getId())
                .subsidiary(getSubsidiary())
                .serviceArea(getServiceArea().getCode())
                .language(getLanguage())
                .aboutCompany(getAboutCompany())
                .importGeneralConditions(getImportGeneralConditions())
                .exportGeneralConditions(getExportGeneralConditions())
                .build();
    }
}
