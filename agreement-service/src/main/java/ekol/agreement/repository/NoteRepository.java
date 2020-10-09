package ekol.agreement.repository;

import ekol.agreement.domain.model.Note;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface NoteRepository extends ApplicationRepository<Note> {

    List<Note> findByAgreement(Agreement agreement);
}
