package ekol.orders.order.domain.dto.json;

public class CreateOrderRequestJson {

    private OrderJson order;
    private boolean confirmed;
    private int numberOfReplications = 1;

    public OrderJson getOrder() {
        return order;
    }

    public void setOrder(OrderJson order) {
        this.order = order;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getNumberOfReplications() {
        return numberOfReplications;
    }

    public void setNumberOfReplications(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
    }
}
