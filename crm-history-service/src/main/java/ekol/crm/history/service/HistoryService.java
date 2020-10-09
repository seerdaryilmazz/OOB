package ekol.crm.history.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.*;
import org.javers.core.diff.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.history.domain.*;
import ekol.crm.history.repository.HistoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryService {

    private HistoryRepository repository;

    @Transactional
    public void save(Item item, Object data, String changedBy, LocalDateTime changeTime){

        History history;
        Change change = new Change();
        change.setChangedBy(changedBy);
        change.setChangeTime(changeTime);
        change.setDescription("New record created.");
        change.setChangeObject(data);

        Optional<History> existingHistory = repository.findByItem(item);

        if(existingHistory.isPresent()){
            history = existingHistory.get();
            Javers javers = JaversBuilder.javers().build();
            Diff diff = javers.compare(history.getLatestData(), data);

            if(!diff.hasChanges()){
                return;
            }
            change.setDescription(diff.getChanges().stream().map(x -> x.prettyPrint(PrettyValuePrinter.getDefault()))
                    .collect(Collectors.joining(", ")));
            history.getChanges().add(change);
        }else{
            history = new History();
            history.setItem(item);
            history.setChanges(Arrays.asList(change));
        }
        history.setLatestData(data);
        repository.save(history);
    }

    public List<Change> retrieveChanges(Long id, String type) {
        Item item = new Item();
        item.setId(id);
        item.setType(type);
        return repository.findByItem(item)
        		.map(History::getChanges)
        		.map(Collection::stream)
        		.orElseGet(Stream::empty)
        		.sorted(Comparator.comparing(Change::getChangeTime).reversed())
        		.collect(Collectors.toList());
    }

    public History retrieveLatestData(Long id, String type) {
        Item item = new Item();
        item.setId(id);
        item.setType(type);
        return repository.findByItem(item).orElse(null);
    }
}



