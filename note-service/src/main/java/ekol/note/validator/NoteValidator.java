package ekol.note.validator;


import ekol.exceptions.ValidationException;
import ekol.note.domain.Note;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NoteValidator {

    public void validate(Note note){
        if (note.getType() == null || StringUtils.isBlank(note.getType().getCode())) {
            throw new ValidationException("Note should have a type");
        }
        if (StringUtils.isBlank(note.getContent())) {
            throw new ValidationException("Note should have a content");
        }
    }

}
