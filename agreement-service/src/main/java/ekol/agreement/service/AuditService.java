package ekol.agreement.service;

import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.ModificationStore;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 19.09.2019
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditService {

    @PersistenceContext
    private EntityManager em;

    public <T> List<T> retrieveAuditLogsById(Long id, Class<T> auditClass){
        AuditQuery q = AuditReaderFactory.get(em).createQuery().forRevisionsOfEntity(auditClass, true, true);
        q.add(AuditEntity.id().eq(id));
        return q.getResultList();
    }

    public <T> T retrieveLatestRevision(Long id, Class<T> auditClass){
        AuditReader reader = AuditReaderFactory.get(em);
        List<Number> revNumbers = reader.getRevisions(auditClass, id);
        return reader.find(auditClass, id, revNumbers.get(revNumbers.size()-2));
    }
}

