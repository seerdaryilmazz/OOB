package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrPackageType;
import ekol.orders.order.domain.AdrUnit;
import ekol.orders.order.domain.OrderShipmentAdr;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentAdrJson {
    private Long id;
    private Long adrClassDetailsId;
    private Integer quantity;
    private Integer innerQuantity;
    private IdCodeNameTrio packageType;
    private IdCodeNameTrio innerPackageType;
    private BigDecimal amount;
    private AdrUnit unit;

    public OrderShipmentAdr toEntity(){
        OrderShipmentAdr adr = new OrderShipmentAdr();
        adr.setId(getId());
        adr.setAdrClassDetails(AdrClassDetails.with(getAdrClassDetailsId()));
        adr.setQuantity(getQuantity());
        adr.setInnerQuantity(getInnerQuantity());
        if(getPackageType() != null){
            adr.setPackageType(AdrPackageType.with(getPackageType()));
        }
        if(getInnerPackageType() != null){
            adr.setInnerPackageType(AdrPackageType.with(getInnerPackageType()));
        }
        adr.setAmount(getAmount());
        adr.setUnit(getUnit());
        return adr;
    }

    public static OrderShipmentAdrJson fromEntity(OrderShipmentAdr adr){
        OrderShipmentAdrJson json = new OrderShipmentAdrJson();
        json.setId(adr.getId());
        json.setAdrClassDetailsId(adr.getAdrClassDetails().getId());
        json.setQuantity(adr.getQuantity());
        json.setInnerQuantity(adr.getInnerQuantity());
        if(adr.getPackageType() != null){
            json.setPackageType(IdCodeNameTrio.with(adr.getPackageType().getId(), adr.getPackageType().getCode(), adr.getPackageType().getName()));
        }
        if(adr.getInnerPackageType() != null){
            json.setInnerPackageType(IdCodeNameTrio.with(adr.getInnerPackageType().getId(), adr.getInnerPackageType().getCode(), adr.getInnerPackageType().getName()));
        }
        json.setAmount(adr.getAmount());
        json.setUnit(adr.getUnit());
        return json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdrClassDetailsId() {
        return adrClassDetailsId;
    }

    public void setAdrClassDetailsId(Long adrClassDetailsId) {
        this.adrClassDetailsId = adrClassDetailsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getInnerQuantity() {
        return innerQuantity;
    }

    public void setInnerQuantity(Integer innerQuantity) {
        this.innerQuantity = innerQuantity;
    }

    public IdCodeNameTrio getPackageType() {
        return packageType;
    }

    public void setPackageType(IdCodeNameTrio packageType) {
        this.packageType = packageType;
    }

    public IdCodeNameTrio getInnerPackageType() {
        return innerPackageType;
    }

    public void setInnerPackageType(IdCodeNameTrio innerPackageType) {
        this.innerPackageType = innerPackageType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AdrUnit getUnit() {
        return unit;
    }

    public void setUnit(AdrUnit unit) {
        this.unit = unit;
    }
}
