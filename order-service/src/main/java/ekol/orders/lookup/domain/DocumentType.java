package ekol.orders.lookup.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

@Entity
@Table(name = "document_type")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_document_type", sequenceName = "seq_document_type")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_document_type")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentTypeGroup documentGroup;

    public static DocumentType with(Long id, String name){
        DocumentType type = new DocumentType();
        type.setId(id);
        type.setName(name);
        return type;
    }
    public static DocumentType with(IdNamePair idName){
        return DocumentType.with(idName.getId(), idName.getName());
    }

    public static DocumentType with(Long id, String code, String name){
    	DocumentType type = new DocumentType();
    	type.setId(id);
    	type.setCode(code);
    	type.setName(name);
    	return type;
    }
    public static DocumentType with(IdCodeNameTrio idCodeName){
    	return DocumentType.with(idCodeName.getId(), idCodeName.getCode(), idCodeName.getName());
    }

    public IdNamePair toIdNamePair(){
        return IdNamePair.with(getId(), getName());
    }
    public IdCodeNameTrio toIdCodeNameTrio(){
    	return IdCodeNameTrio.with(getId(), getCode(), getName());
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public DocumentTypeGroup getDocumentGroup() {
        return documentGroup;
    }

    public void setDocumentGroup(DocumentTypeGroup documentGroup) {
        this.documentGroup = documentGroup;
    }
}