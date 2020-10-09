package ekol.orders.lookup.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class DocumentTypeRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<DocumentType> {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public Class<DocumentType> getType() {
        return DocumentType.class;
    }

    @Override
    public LookupRepository<DocumentType> getRepository() {
        return documentTypeRepository;
    }
}
