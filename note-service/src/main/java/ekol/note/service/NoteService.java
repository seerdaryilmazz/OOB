package ekol.note.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.note.domain.Note;
import ekol.note.repository.NoteRepository;
import ekol.note.validator.NoteValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NoteService {

    private NoteRepository repository;
    private NoteValidator validator;


    @Transactional
    public Note save(Note note){
        if(!StringUtils.isEmpty(note.getId())){
            Note savedNote = getById(note.getId());
            note.setCreatedAt(savedNote.getCreatedAt());
            note.setCreatedBy(savedNote.getCreatedBy());
            note.setVersion(savedNote.getVersion());
        }
        validator.validate(note);
        return repository.save(note);
    }

    public Note getById(String id) {
        return Optional.ofNullable(repository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Note with id {0} not found", id));
    }
    
    public Iterable<Note> listById(Iterable<String> id){
    	return repository.findAll(id);
    }

}



