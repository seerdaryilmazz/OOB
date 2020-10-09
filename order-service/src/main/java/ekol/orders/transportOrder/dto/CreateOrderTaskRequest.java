package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.TransportOrderRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 30/09/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderTaskRequest {

    private TransportOrderRequest orderRequest;
    private List<CustomerRepTeam> customerRepTeams = new ArrayList<>();
    private Company customer;
    private String callbackUrl;
    private String taskId;

    public static CreateOrderTaskRequest withOrderRequest(TransportOrderRequest orderRequest){
        CreateOrderTaskRequest request = new CreateOrderTaskRequest();
        request.setOrderRequest(orderRequest);
        return request;
    }
    public TransportOrderRequest getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(TransportOrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    public List<CustomerRepTeam> getCustomerRepTeams() {
        return customerRepTeams;
    }

    public void setCustomerRepTeams(List<CustomerRepTeam> customerRepTeams) {
        this.customerRepTeams = customerRepTeams;
    }

    public Company getCustomer() {
        return customer;
    }

    public void setCustomer(Company customer) {
        this.customer = customer;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @JsonProperty
    public String getCustomerRepTeamTags(){
        StringBuilder sb = new StringBuilder(32);
        getCustomerRepTeams().forEach(team -> team.getTags().forEach(tag -> {
            if(sb.length() > 0){
                sb.append("|");
            }
            sb.append(tag.getValue());
        }));
        return sb.toString();
    }
}
