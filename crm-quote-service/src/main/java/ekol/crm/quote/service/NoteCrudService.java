package ekol.crm.quote.service;

import ekol.crm.quote.domain.dto.NoteJson;
import ekol.crm.quote.domain.model.Note;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.NoteRepository;
import ekol.crm.quote.validator.NoteValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NoteCrudService {

    private NoteRepository repository;
    private NoteValidator validator;

    @Transactional
    public List<Note> save(Quote quote, List<NoteJson> request){


        List<Note> notes = new ArrayList<>();
        List<Note> existing = quote.getNotes();
        if(!CollectionUtils.isEmpty(request)){
            notes.addAll(request.stream()
                    .map(noteJson -> {
                        Note note = noteJson.toEntity();
                        note.setQuote(quote);
                        this.validator.validate(note);
                        return note;
                    }).collect(Collectors.toList()));
        }

        notes = IteratorUtils.toList(this.repository.save(notes).iterator());


        // delete discarded notes
        deleteDiscardedNotes(existing, notes);

        return notes;
    }

    private void deleteDiscardedNotes(List<Note> existing, List<Note> saved) {
        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedNoteIds = saved.stream().map(Note::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedNoteIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(Note note){
        note.setDeleted(true);
        repository.save(note);
    }

    public List<Note> getByQuote(Quote quote){
        return repository.findByQuote(quote);
    }

}
