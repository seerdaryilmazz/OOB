package ekol.crm.quote.service;

import ekol.crm.quote.domain.dto.DocumentJson;
import ekol.crm.quote.domain.model.Document;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.DocumentRepository;
import ekol.crm.quote.validator.DocumentValidator;
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

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentCrudService {

    private DocumentRepository repository;
    private DocumentValidator validator;

    @Transactional
    public List<Document> save(Quote quote, List<DocumentJson> request){


        List<Document> documents = new ArrayList<>();
        List<Document> existing = quote.getDocuments();
        if(!CollectionUtils.isEmpty(request)){
            documents.addAll(request.stream()
                    .map(documentJson -> {
                        Document document = documentJson.toEntity();
                        document.setQuote(quote);
                        this.validator.validate(document);
                        return document;
                    }).collect(Collectors.toList()));
        }

        documents = IteratorUtils.toList(this.repository.save(documents).iterator());


        // delete discarded documents
        deleteDiscardedDocuments(existing, documents);

        return documents;
    }

    @Transactional
    public List<Document> savePdf(Quote quote, List<DocumentJson> request){


        List<Document> documents = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request)){
            documents.addAll(request.stream()
                    .map(documentJson -> {
                        Document document = documentJson.toEntity();
                        document.setQuote(quote);
                        this.validator.validate(document);
                        return document;
                    }).collect(Collectors.toList()));
        }

        documents = IteratorUtils.toList(this.repository.save(documents).iterator());

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

    public List<Document> getByQuote(Quote quote){
        return repository.findByQuote(quote);
    }
    
    public Document save(Document document) {
    	return repository.save(document);
    }

}
