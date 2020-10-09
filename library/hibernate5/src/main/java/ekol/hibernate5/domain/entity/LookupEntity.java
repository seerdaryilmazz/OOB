package ekol.hibernate5.domain.entity;

import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@CustomSchema(CustomSchemaType.LOOKUP)
public abstract class LookupEntity extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    public abstract Long getId();
    public abstract void setId(Long id);

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

}
