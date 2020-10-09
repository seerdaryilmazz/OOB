package ekol.file.repository;

import ekol.file.domain.FileEntry;
import org.springframework.data.repository.CrudRepository;

public interface FileEntryRepository extends CrudRepository<FileEntry, String> {
}
