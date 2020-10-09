package ekol.orders.order.domain;

import ekol.model.CodeNamePair;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;

public class CodeNameEmbeddable {

    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    public static CodeNameEmbeddable with(String code, String name){
        CodeNameEmbeddable codeName = new CodeNameEmbeddable();
        codeName.setCode(code);
        codeName.setName(name);
        return codeName;
    }

    public static CodeNameEmbeddable with(CodeNamePair codeNamePair){
        return CodeNameEmbeddable.with(codeNamePair.getCode(), codeNamePair.getName());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CodeNamePair toCodeNamePair(){
        return CodeNamePair.with(getCode(), getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getCode()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CodeNameEmbeddable))
            return false;
        if (object == this)
            return true;

        CodeNameEmbeddable codeName = (CodeNameEmbeddable) object;
        return new EqualsBuilder().
                append(getCode(), codeName.getCode()).
                append(getName(), codeName.getName()).
                isEquals();
    }
}
