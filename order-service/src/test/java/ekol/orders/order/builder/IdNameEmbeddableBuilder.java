package ekol.orders.order.builder;

import ekol.orders.order.domain.IdNameEmbeddable;

public final class IdNameEmbeddableBuilder {
    private Long id;
    private String name;

    private IdNameEmbeddableBuilder() {
    }

    public static IdNameEmbeddableBuilder anIdNameEmbeddable() {
        return new IdNameEmbeddableBuilder();
    }

    public IdNameEmbeddableBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public IdNameEmbeddableBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IdNameEmbeddableBuilder but() {
        return anIdNameEmbeddable().withId(id).withName(name);
    }

    public IdNameEmbeddable build() {
        IdNameEmbeddable idNameEmbeddable = new IdNameEmbeddable();
        idNameEmbeddable.setId(id);
        idNameEmbeddable.setName(name);
        return idNameEmbeddable;
    }
}
