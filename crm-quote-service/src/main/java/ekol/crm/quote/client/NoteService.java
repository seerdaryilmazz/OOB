package ekol.crm.quote.client;

import static java.util.stream.Collectors.joining;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.noteservice.Note;

@Service
public class NoteService {

	@Value("${oneorder.note-service}")
    private String noteServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public Note findNotesById(String id) {
        return restTemplate.getForObject(noteServiceName + "/note/{id}", Note.class, id);
    }
    
    public List<Note> findNotesByIds(List<String> id) {
    	return Arrays.asList(restTemplate.getForObject(noteServiceName + "/note?id={ids}", Note[].class, id.stream().collect(joining(","))));
    }
}


