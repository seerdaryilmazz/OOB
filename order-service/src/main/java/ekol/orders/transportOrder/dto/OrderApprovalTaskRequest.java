package ekol.orders.transportOrder.dto;


import ekol.orders.transportOrder.domain.TransportOrder;

/**
 * Created by ozer on 14/02/2017.
 */
public class OrderApprovalTaskRequest {

    private TransportOrder transportOrder;
    private boolean supervisorApprovalNeeded;
    private boolean managerApprovalNeeded;
    private String supervisorAccountName;
    private String managerAccountName;

    public OrderApprovalTaskRequest() {
    }

    public OrderApprovalTaskRequest(TransportOrder transportOrder, boolean supervisorApprovalNeeded, boolean managerApprovalNeeded, String supervisorAccountName, String managerAccountName) {
        this.transportOrder = transportOrder;
        this.supervisorApprovalNeeded = supervisorApprovalNeeded;
        this.managerApprovalNeeded = managerApprovalNeeded;
        this.supervisorAccountName = supervisorAccountName;
        this.managerAccountName = managerAccountName;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public boolean isSupervisorApprovalNeeded() {
        return supervisorApprovalNeeded;
    }

    public void setSupervisorApprovalNeeded(boolean supervisorApprovalNeeded) {
        this.supervisorApprovalNeeded = supervisorApprovalNeeded;
    }

    public boolean isManagerApprovalNeeded() {
        return managerApprovalNeeded;
    }

    public void setManagerApprovalNeeded(boolean managerApprovalNeeded) {
        this.managerApprovalNeeded = managerApprovalNeeded;
    }

    public String getSupervisorAccountName() {
        return supervisorAccountName;
    }

    public void setSupervisorAccountName(String supervisorAccountName) {
        this.supervisorAccountName = supervisorAccountName;
    }

    public String getManagerAccountName() {
        return managerAccountName;
    }

    public void setManagerAccountName(String managerAccountName) {
        this.managerAccountName = managerAccountName;
    }
}
