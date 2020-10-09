package ekol.agreement.domain.model;

import ekol.agreement.domain.dto.agreement.HistoryModelJson;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "HistoryAdpModel")
@SequenceGenerator(name = "SEQ_HISTORYMODEL", sequenceName = "SEQ_HISTORYMODEL")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HistoryModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORYMODEL")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 3)
    private Integer eur;

    @Column(precision = 3)
    private Integer usd;

    @Column(precision = 3)
    private Integer inflation;

    @Column(precision = 3)
    private Integer minimumWage;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column
    private LocalDate validityEndDate;

    @Column
    private String notes;

    @Column
    private Long modelId;

    public HistoryModelJson toJson(){
        HistoryModelJson json = new HistoryModelJson();
        json.setId(getId());
        json.setName(getName());
        json.setEur(getEur());
        json.setUsd(getUsd());
        json.setInflation(getInflation());
        json.setMinimumWage(getMinimumWage());
        json.setValidityStartDate(getValidityStartDate());
        json.setValidityEndDate(getValidityEndDate());
        json.setNotes(getNotes());
        json.setModelId(getModelId());
        json.setLastUpdated(getLastUpdated());
        return json;
    }
}
