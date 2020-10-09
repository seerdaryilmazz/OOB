package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.serializers.LocalDateDeserializer;
import ekol.json.serializers.LocalDateSerializer;
import ekol.kartoteks.domain.CompanyRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleExchangeData {

    private Long kartoteksId;
    private String segmentCode;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    private String relationCode;
    private List<EmployeeCustomerRelationExchangeData> relations = new ArrayList<>();

    public static RoleExchangeData fromCompanyRole(CompanyRole role){
        RoleExchangeData exchangeData = new RoleExchangeData();
        exchangeData.setKartoteksId(role.getId());
        exchangeData.setSegmentCode(role.getSegmentType() != null ? role.getSegmentType().getCode(): null);
        exchangeData.setStartDate(role.getDateRange() != null ? role.getDateRange().getStartDate() : null);
        exchangeData.setEndDate(role.getDateRange() != null ? role.getDateRange().getEndDate() : null);
        exchangeData.setRelationCode(role.getRoleType() != null ? role.getRoleType().getCode() : null);
        role.getEmployeeRelations().forEach(employeeRelation ->
                exchangeData.getRelations().add(EmployeeCustomerRelationExchangeData.fromCompanyRelation(employeeRelation))
        );

        return exchangeData;
    }

    public Long getKartoteksId() {
        return kartoteksId;
    }

    public void setKartoteksId(Long kartoteksId) {
        this.kartoteksId = kartoteksId;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public List<EmployeeCustomerRelationExchangeData> getRelations() {
        return relations;
    }

    public void setRelations(List<EmployeeCustomerRelationExchangeData> relations) {
        this.relations = relations;
    }

}
