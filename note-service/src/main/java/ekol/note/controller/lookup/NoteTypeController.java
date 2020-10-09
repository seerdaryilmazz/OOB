package ekol.note.controller.lookup;

import ekol.note.domain.NoteType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/note-type")
public class NoteTypeController extends BaseEnumApiController<NoteType> {

    @PostConstruct
    public void init() {
        setType(NoteType.class);
    }
}
