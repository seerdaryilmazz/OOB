package ekol.orders.order.domain;

import ekol.model.IdNamePair;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IdNameEmbeddable {

    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    public static IdNameEmbeddable with(Long id, String name){
        IdNameEmbeddable idName = new IdNameEmbeddable();
        idName.setId(id);
        idName.setName(name);
        return idName;
    }

    public static IdNameEmbeddable with(IdNamePair idNamePair){
        return IdNameEmbeddable.with(idNamePair.getId(), idNamePair.getName());
    }

    public IdNamePair toIdNamePair(){
        return IdNamePair.with(getId(), getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getId()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IdNameEmbeddable))
            return false;
        if (object == this)
            return true;

        IdNameEmbeddable idName = (IdNameEmbeddable) object;
        return new EqualsBuilder().
                append(getId(), idName.getId()).
                append(getName(), idName.getName()).
                isEquals();
    }
}
