package ekol.orders.order.builder;

import ekol.orders.order.domain.CodeNameEmbeddable;

public final class CodeNameEmbeddableBuilder {
    private String code;
    private String name;

    private CodeNameEmbeddableBuilder() {
    }

    public static CodeNameEmbeddableBuilder aCodeNameEmbeddable() {
        return new CodeNameEmbeddableBuilder();
    }

    public CodeNameEmbeddableBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public CodeNameEmbeddableBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CodeNameEmbeddableBuilder but() {
        return aCodeNameEmbeddable().withCode(code).withName(name);
    }

    public CodeNameEmbeddable build() {
        CodeNameEmbeddable codeNameEmbeddable = new CodeNameEmbeddable();
        codeNameEmbeddable.setCode(code);
        codeNameEmbeddable.setName(name);
        return codeNameEmbeddable;
    }
}
