package ekol.orders.order.domain.dto.json;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.*;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderJson {
    private Long id;
    private String code;
    private IdNamePair customer;
    private IdNamePair originalCustomer;
    private IdNamePair subsidiary;
    private TruckLoadType truckLoadType;
    private ServiceType serviceType;
    private Status status;
    private String countryCode;
    private String templateId;
    private String templateName;
    private List<OrderShipmentJson> shipments = new ArrayList<>();
    private List<DocumentJson> documents = new ArrayList<>();
    private String createdBy;
    private String lastUpdatedBy;
    private boolean initial;

    public Order toEntity(){
        Order order = new Order();
        order.setId(getId());
        order.setCode(getCode());
        order.setCustomer(IdNameEmbeddable.with(getCustomer()));
        if(getOriginalCustomer() != null){
            order.setOriginalCustomer(IdNameEmbeddable.with(getOriginalCustomer()));
        }
        order.setSubsidiary(IdNameEmbeddable.with(getSubsidiary()));
        order.setServiceType(getServiceType());
        order.setTruckLoadType(getTruckLoadType());
        order.setStatus(getStatus());
        order.setCountryCode(getCountryCode());
        order.setTemplateId(getTemplateId());
        order.setTemplateName(getTemplateName());
        order.setShipments(getShipments().stream().map(OrderShipmentJson::toEntity).collect(toList()));
        order.setDocuments(getDocuments().stream().map(DocumentJson::toOrderDocument).collect(toList()));
        order.setInitial(isInitial());
        return order;
    }

    public static OrderJson fromEntity(Order order){
        OrderJson json = new OrderJson();
        json.setId(order.getId());
        json.setCode(order.getCode());
        json.setCustomer(order.getCustomer().toIdNamePair());
        if(order.getOriginalCustomer() != null){
            json.setOriginalCustomer(order.getOriginalCustomer().toIdNamePair());
        }
        json.setSubsidiary(order.getSubsidiary().toIdNamePair());
        json.setServiceType(order.getServiceType());
        json.setTruckLoadType(order.getTruckLoadType());
        json.setStatus(order.getStatus());
        json.setCountryCode(order.getCountryCode());
        json.setTemplateId(order.getTemplateId());
        json.setTemplateName(order.getTemplateName());
        json.setShipments(order.getShipments().stream().map(OrderShipmentJson::fromEntity).collect(toList()));
        json.setDocuments(order.getDocuments().stream().map(DocumentJson::fromOrderDocument).collect(toList()));
        json.setCreatedBy(order.getCreatedBy());
        json.setLastUpdatedBy(order.getLastUpdatedBy());
        json.setInitial(order.isInitial());
        return json;
    }
}
