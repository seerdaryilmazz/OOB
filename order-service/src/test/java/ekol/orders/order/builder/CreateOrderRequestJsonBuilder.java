package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.json.CreateOrderRequestJson;
import ekol.orders.order.domain.dto.json.OrderJson;

public final class CreateOrderRequestJsonBuilder {
    private OrderJson order;
    private boolean confirmed;
    private int numberOfReplications = 1;

    private CreateOrderRequestJsonBuilder() {
    }

    public static CreateOrderRequestJsonBuilder aCreateOrderRequestJson() {
        return new CreateOrderRequestJsonBuilder();
    }

    public CreateOrderRequestJsonBuilder withOrder(OrderJson order) {
        this.order = order;
        return this;
    }

    public CreateOrderRequestJsonBuilder withConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public CreateOrderRequestJsonBuilder withNumberOfReplications(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
        return this;
    }

    public CreateOrderRequestJsonBuilder but() {
        return aCreateOrderRequestJson().withOrder(order).withConfirmed(confirmed).withNumberOfReplications(numberOfReplications);
    }

    public CreateOrderRequestJson build() {
        CreateOrderRequestJson createOrderRequestJson = new CreateOrderRequestJson();
        createOrderRequestJson.setOrder(order);
        createOrderRequestJson.setConfirmed(confirmed);
        createOrderRequestJson.setNumberOfReplications(numberOfReplications);
        return createOrderRequestJson;
    }
}
