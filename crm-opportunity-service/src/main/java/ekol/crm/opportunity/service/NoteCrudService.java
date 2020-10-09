package ekol.crm.opportunity.service;

import ekol.crm.opportunity.domain.dto.NoteJson;
import ekol.crm.opportunity.domain.model.Note;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.crm.opportunity.repository.NoteRepository;
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

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NoteCrudService {

    private NoteRepository repository;

    @Transactional
    public List<Note> save(Opportunity opportunity, List<NoteJson> request){


        List<Note> notes = new ArrayList<>();
        List<Note> existing = opportunity.getNotes();
        if(!CollectionUtils.isEmpty(request)){
            notes.addAll(request.stream()
                    .map(noteJson -> {
                        Note note = noteJson.toEntity();
                        note.setOpportunity(opportunity);
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

    public List<Note> getByOpportunity(Opportunity opportunity){
        return repository.findByOpportunity(opportunity);
    }

}
