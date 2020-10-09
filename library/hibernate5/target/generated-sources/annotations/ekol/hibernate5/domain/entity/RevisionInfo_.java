package ekol.hibernate5.domain.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RevisionInfo.class)
public abstract class RevisionInfo_ {

	public static volatile SingularAttribute<RevisionInfo, Date> updatedTime;
	public static volatile SingularAttribute<RevisionInfo, String> updatedBy;
	public static volatile SingularAttribute<RevisionInfo, Long> id;

}

