package ekol.crm.quote.domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.util.BeanUtils;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.IdNamePair;
import lombok.*;

@Entity
@Table(name = "CrmSpotQuotePdfSetting")
@SequenceGenerator(name = "SEQ_CRM_SPOT_QUOTE_PDF_SETTING", sequenceName = "SEQ_CRM_SPOT_QUOTE_PDF_SETTING")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpotQuotePdfSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRM_SPOT_QUOTE_PDF_SETTING")
    private Long id;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name="SUBSIDIARY_ID")),
            @AttributeOverride(name = "name", column = @Column(name="SUBSIDIARY_NAME"))})
    private IdNamePair subsidiary;

    @Column
    private String serviceArea;

    /**
     * translator-service'teki SupportedLocale'e referans...
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "LANGUAGE_ID")),
            @AttributeOverride(name = "isoCode", column = @Column(name = "LANGUAGE_ISO_CODE")),
            @AttributeOverride(name = "name", column = @Column(name = "LANGUAGE_NAME"))})
    private SupportedLocale language;

    @Nationalized
    @Column(columnDefinition = "nclob")
    private String aboutCompany;

    /**
     * HTML string
     */
    @Nationalized
    @Column(columnDefinition = "nclob")
    private String importGeneralConditions;

    /**
     * HTML string
     */
    @Nationalized
    @Column(columnDefinition = "nclob")
    private String exportGeneralConditions;

    public SpotQuotePdfSettingJson toJson(){
        SpotQuotePdfSettingJson json = new SpotQuotePdfSettingJson();
        json.setId(getId());
        json.setSubsidiary(getSubsidiary());
        json.setServiceArea(BeanUtils.getBean(KartoteksService.class).findServiceAreaByCode(getServiceArea(),true));
        json.setLanguage(getLanguage());
        json.setAboutCompany(getAboutCompany());
        json.setImportGeneralConditions(getImportGeneralConditions());
        json.setExportGeneralConditions(getExportGeneralConditions());
        return json;
    }
}
