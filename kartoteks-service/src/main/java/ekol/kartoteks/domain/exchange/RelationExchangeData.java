package ekol.kartoteks.domain.exchange;


import ekol.kartoteks.domain.CompanyRelationType;

/**
 * Created by kilimci on 25/06/16.
 */
public class RelationExchangeData {

    private String relationTypeCode;
    private Long kartoteksId;

    public static RelationExchangeData fromCompanyRelation(CompanyRelationType relationType, Long companyId){
        RelationExchangeData exchangeData = new RelationExchangeData();
        exchangeData.setRelationTypeCode(relationType.getCode());
        exchangeData.setKartoteksId(companyId);
        return exchangeData;
    }

    public String getRelationTypeCode() {
        return relationTypeCode;
    }

    public void setRelationTypeCode(String relationTypeCode) {
        this.relationTypeCode = relationTypeCode;
    }

    public Long getKartoteksId() {
        return kartoteksId;
    }

    public void setKartoteksId(Long kartoteksId) {
        this.kartoteksId = kartoteksId;
    }
}
