package ekol.crm.quote.domain.model;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.dto.SpotQuotePdfSettingJson;
import ekol.crm.quote.domain.dto.SupportedLocale;
import ekol.crm.quote.util.BeanUtils;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * SpotQuotePdfSetting sınıfı varken neden böyle bir sınıfa ihtiyaç duyduk?
 * SpotQuotePdfSetting sınıfında LOB alanlar var ve "fetch = FetchType.LAZY" ayarı LOB alanlar için işe yaramıyor. Bu yüzden,
 * ekranda listeleme yapacağımız zaman sorguları bu sınıfın repository'si ile yapıyoruz (tabi LOB alanları ekranda göstermeyeceksek).
 */
@Entity
@Table(name = "CrmSpotQuotePdfSetting")
@SequenceGenerator(name = "SEQ_CRM_SPOT_QUOTE_PDF_SETTING", sequenceName = "SEQ_CRM_SPOT_QUOTE_PDF_SETTING")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpotQuotePdfSettingForListing extends BaseEntity {

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

    public SpotQuotePdfSettingJson toJson(){
        SpotQuotePdfSettingJson json = new SpotQuotePdfSettingJson();
        json.setId(getId());
        json.setSubsidiary(getSubsidiary());
        json.setServiceArea(BeanUtils.getBean(KartoteksService.class).findServiceAreaByCode(getServiceArea(),true));
        json.setLanguage(getLanguage());
        return json;
    }
}
