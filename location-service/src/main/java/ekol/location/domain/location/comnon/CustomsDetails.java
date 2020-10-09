package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.customs.CustomsOffice;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="plCustomsDetails")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomsDetails extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_pl_customs_details", sequenceName = "seq_pl_customs_details")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pl_customs_details")
    private Long id;

    @Enumerated(EnumType.STRING)
    private WarehouseCustomsType customsType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customsOfficeId")
    @JsonSerialize(using = CustomsOfficeSerializer.class)
    private CustomsOffice customsOffice;

    @Column(length = 20)
    private String europeanCustomsCode;

    @Column(length = 20)
    private String warehouseCode;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean onBoardCustomsClearance;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean usedForDangerousGoods;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean usedForTempControlledGoods;

    @Column(length = 9)
    private String dangerousGoodsCode;

    @Column(length = 9)
    private String tempControlledGoodsCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WarehouseCustomsType getCustomsType() {
        return customsType;
    }

    public void setCustomsType(WarehouseCustomsType customsType) {
        this.customsType = customsType;
    }

    public CustomsOffice getCustomsOffice() {
        return customsOffice;
    }

    public void setCustomsOffice(CustomsOffice customsOffice) {
        this.customsOffice = customsOffice;
    }

    public String getEuropeanCustomsCode() {
        return europeanCustomsCode;
    }

    public void setEuropeanCustomsCode(String europeanCustomsCode) {
        this.europeanCustomsCode = europeanCustomsCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public boolean isOnBoardCustomsClearance() {
        return onBoardCustomsClearance;
    }

    public void setOnBoardCustomsClearance(boolean onBoardCustomsClearance) {
        this.onBoardCustomsClearance = onBoardCustomsClearance;
    }

    public boolean isUsedForDangerousGoods() {
        return usedForDangerousGoods;
    }

    public void setUsedForDangerousGoods(boolean usedForDangerousGoods) {
        this.usedForDangerousGoods = usedForDangerousGoods;
    }

    public boolean isUsedForTempControlledGoods() {
        return usedForTempControlledGoods;
    }

    public void setUsedForTempControlledGoods(boolean usedForTempControlledGoods) {
        this.usedForTempControlledGoods = usedForTempControlledGoods;
    }

    public String getDangerousGoodsCode() {
        return dangerousGoodsCode;
    }

    public void setDangerousGoodsCode(String dangerousGoodsCode) {
        this.dangerousGoodsCode = dangerousGoodsCode;
    }

    public String getTempControlledGoodsCode() {
        return tempControlledGoodsCode;
    }

    public void setTempControlledGoodsCode(String tempControlledGoodsCode) {
        this.tempControlledGoodsCode = tempControlledGoodsCode;
    }
}
