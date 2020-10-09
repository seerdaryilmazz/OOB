package ekol.note.repository;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.note.domain.Note;


public interface NoteRepository extends ApplicationMongoRepository<Note> { }
