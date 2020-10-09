package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.order.domain.Document;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentValidator {

    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentValidator(DocumentTypeRepository documentTypeRepository){
        this.documentTypeRepository = documentTypeRepository;
    }

    public void validate(Document orderDocument){
        if(orderDocument == null || orderDocument.getType() == null || orderDocument.getType().getId() == null){
            throw new ValidationException("Document should have a type");
        }
        if(!documentTypeRepository.findById(orderDocument.getType().getId()).isPresent()){
            throw new ValidationException("Document type with id {0} does not exist", orderDocument.getType().getId().toString());
        }
        if(StringUtils.isBlank(orderDocument.getSavedFileName())){
            throw new ValidationException("Document should have a file name");
        }
        if(StringUtils.isBlank(orderDocument.getOriginalFileName())){
            throw new ValidationException("Document should have a file name");
        }
    }

    public void validate(List<Document> documents){
        documents.forEach(this::validate);
    }


}
