package ekol.crm.account.repository;

import ekol.crm.account.domain.model.Contact;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends ApplicationRepository<Contact>{

    Optional<Contact> findById(Long id);
    List<Contact> findByAccountId(Long accountId);
    long countByCompanyContactIdAndAccountId(Long companyContactId, Long accountId);
}
