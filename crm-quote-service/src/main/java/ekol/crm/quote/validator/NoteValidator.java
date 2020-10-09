package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Note;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class NoteValidator {

    public void validate(Note note){
        if(note.getQuote() == null || note.getQuote().getId() == null){
            throw new ValidationException("Note should be assigned to a quote");
        }
        if(StringUtils.isEmpty(note.getNoteId())){
            throw new ValidationException("Note should have a id");
        }
        if(note.getType() == null || StringUtils.isEmpty(note.getType().getCode())){
            throw new ValidationException("Note should have a type");
        }
    }
}
