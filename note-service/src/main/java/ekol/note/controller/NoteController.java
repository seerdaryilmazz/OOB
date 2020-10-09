package ekol.note.controller;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.note.domain.dto.NoteJson;
import ekol.note.service.NoteService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/note")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NoteController {

    private NoteService service;

    @PostMapping
    public NoteJson createNote(@RequestBody NoteJson request) {
        request.validate();
        return NoteJson.fromEntity(service.save(request.toEntity()));
    }

    @PutMapping("/{id}")
    public NoteJson updateNote(@PathVariable String id, @RequestBody NoteJson request) {
        request.setId(id);
        request.validate();
        return NoteJson.fromEntity(service.save(request.toEntity()));
    }

    @GetMapping("/{id}")
    public NoteJson retrieveNoteById(@PathVariable String id) {
        return NoteJson.fromEntity(service.getById(id));
    }
    
    @GetMapping
    public Iterable<NoteJson> retrieveNotes(@RequestParam String[] id) {
		return StreamSupport.stream(service.listById(Arrays.asList(id)).spliterator(), true).map(NoteJson::fromEntity).collect(Collectors.toList());
    }

}
