package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.*;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentCustomsJson {

    private Long id;
    private IdNamePair customsAgent;
    private IdNamePair customsAgentLocation;
    private IdNamePair customsOffice;
    private IdNamePair customsLocation;
    private CodeNamePair customsType;
    private CompanyType customsAgentType;


    public OrderShipmentArrivalCustoms toArrivalEntity(){
        OrderShipmentArrivalCustoms customs = new OrderShipmentArrivalCustoms();
        customs.setId(getId());
        if(getCustomsAgent() != null) {
            customs.setCustomsAgent(IdNameEmbeddable.with(getCustomsAgent()));
            customs.setCustomsAgentType(getCustomsAgentType());
        }
        if(getCustomsAgentLocation() != null) {
            customs.setCustomsAgentLocation(IdNameEmbeddable.with(getCustomsAgentLocation()));
        }
        if(getCustomsOffice() != null){
            customs.setCustomsOffice(IdNameEmbeddable.with(getCustomsOffice()));
        }
        if(getCustomsLocation() != null){
            customs.setCustomsLocation(IdNameEmbeddable.with(getCustomsLocation()));
        }
        if(getCustomsType() != null) {
            customs.setCustomsType(CodeNameEmbeddable.with(getCustomsType()));
        }
        return customs;
    }

    public OrderShipmentDepartureCustoms toDepartureEntity(){
        OrderShipmentDepartureCustoms customs = new OrderShipmentDepartureCustoms();
        customs.setId(getId());
        if(getCustomsAgent() != null) {
            customs.setCustomsAgent(IdNameEmbeddable.with(getCustomsAgent()));
            customs.setCustomsAgentType(getCustomsAgentType());
        }
        if(getCustomsAgentLocation() != null) {
            customs.setCustomsAgentLocation(IdNameEmbeddable.with(getCustomsAgentLocation()));
        }
        if(getCustomsOffice() != null){
            customs.setCustomsOffice(IdNameEmbeddable.with(getCustomsOffice()));
        }
        return customs;
    }

    public static OrderShipmentCustomsJson fromEntity(OrderShipmentArrivalCustoms customs){
        OrderShipmentCustomsJson json = new OrderShipmentCustomsJson();
        json.setId(customs.getId());
        if(customs.getCustomsAgent() != null){
            json.setCustomsAgent(customs.getCustomsAgent().toIdNamePair());
            json.setCustomsAgentType(customs.getCustomsAgentType());
        }
        if(customs.getCustomsAgentLocation() != null){
            json.setCustomsAgentLocation(customs.getCustomsAgentLocation().toIdNamePair());
        }
        if(customs.getCustomsOffice() != null){
            json.setCustomsOffice(customs.getCustomsOffice().toIdNamePair());
        }
        if(customs.getCustomsLocation() != null){
            json.setCustomsLocation(customs.getCustomsLocation().toIdNamePair());
        }
        if(customs.getCustomsType() != null){
            json.setCustomsType(customs.getCustomsType().toCodeNamePair());
        }
        return json;
    }

    public static OrderShipmentCustomsJson fromEntity(OrderShipmentDepartureCustoms customs){
        OrderShipmentCustomsJson json = new OrderShipmentCustomsJson();
        json.setId(customs.getId());
        if(customs.getCustomsAgent() != null){
            json.setCustomsAgent(customs.getCustomsAgent().toIdNamePair());
            json.setCustomsAgentType(customs.getCustomsAgentType());
        }
        if(customs.getCustomsAgentLocation() != null){
            json.setCustomsAgentLocation(customs.getCustomsAgentLocation().toIdNamePair());
        }
        if(customs.getCustomsOffice() != null){
            json.setCustomsOffice(customs.getCustomsOffice().toIdNamePair());
        }
        return json;
    }
}
