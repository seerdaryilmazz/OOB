package ekol.orders.lookup.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/lookup/doc-type")
public class DocumentTypeController extends BaseLookupApiController<DocumentType> {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(documentTypeRepository);
    }

    @RequestMapping(value={"/health-certificates"},method= RequestMethod.GET)
    public List<DocumentType> findHealthCertificates(){
        return findByDocumentGroup(DocumentTypeGroup.HEALTH_CERTIFICATE);
    }
    @RequestMapping(value={"/dangerous-goods"},method= RequestMethod.GET)
    public List<DocumentType> findDangerousGoods(){
        return findByDocumentGroup(DocumentTypeGroup.DANGEROUS_GOODS);
    }
    @RequestMapping(value={"/filter"},method= RequestMethod.GET)
    public List<DocumentType> findShipmentDocumentTypes(@RequestParam(required = false) List<DocumentTypeGroup> exclude,
                                                        @RequestParam(required = false) List<DocumentTypeGroup> include){
        return StreamSupport.stream(documentTypeRepository.findAll().spliterator(), false)
                .filter(type -> (exclude != null && !exclude.contains(type.getDocumentGroup()) ||
                        (include != null && include.contains(type.getDocumentGroup()))))
                .collect(Collectors.toList());

    }
    private List<DocumentType> findByDocumentGroup(DocumentTypeGroup group){
        return documentTypeRepository.findByDocumentGroup(group);
    }
}
