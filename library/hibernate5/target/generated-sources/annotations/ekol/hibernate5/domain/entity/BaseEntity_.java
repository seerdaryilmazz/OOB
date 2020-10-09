package ekol.hibernate5.domain.entity;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ {

	public static volatile SingularAttribute<BaseEntity, UtcDateTime> lastUpdated;
	public static volatile SingularAttribute<BaseEntity, String> lastUpdatedBy;
	public static volatile SingularAttribute<BaseEntity, UtcDateTime> deletedAt;
	public static volatile SingularAttribute<BaseEntity, Boolean> deleted;

}

