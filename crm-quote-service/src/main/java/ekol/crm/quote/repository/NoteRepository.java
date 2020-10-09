package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Note;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface NoteRepository extends ApplicationRepository<Note> {

    List<Note> findByQuote(Quote quote);
}
