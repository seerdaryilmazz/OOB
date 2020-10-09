package ekol.crm.opportunity.service;

import ekol.crm.opportunity.domain.dto.DocumentJson;
import ekol.crm.opportunity.domain.model.Document;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.crm.opportunity.repository.DocumentRepository;
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
public class DocumentCrudService {

    private DocumentRepository repository;

    @Transactional
    public List<Document> save(Opportunity opportunity, List<DocumentJson> request){


        List<Document> documents = new ArrayList<>();
        List<Document> existing = opportunity.getDocuments();
        if(!CollectionUtils.isEmpty(request)){
            documents.addAll(request.stream()
                    .map(documentJson -> {
                        Document document = documentJson.toEntity();
                        document.setOpportunity(opportunity);
                        return document;
                    }).collect(Collectors.toList()));
        }

        documents = IteratorUtils.toList(this.repository.save(documents).iterator());

        // delete discarded documents
        deleteDiscardedDocuments(existing, documents);

        return documents;
    }

    private void deleteDiscardedDocuments(List<Document> existing, List<Document> saved) {
        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedDocumentIds = saved.stream().map(Document::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedDocumentIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(Document document){
        document.setDeleted(true);
        repository.save(document);
    }

    public List<Document> getByOpportunity(Opportunity opportunity){
        return repository.findByOpportunity(opportunity);
    }

}
