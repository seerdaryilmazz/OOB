package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepository extends LookupRepository<DocumentType> {

    List<DocumentType> findByDocumentGroup(DocumentTypeGroup group);

    Optional<DocumentType> findById(Long id);
}
