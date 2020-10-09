package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.Document;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ekol.orders.order.builder.MockOrderData.invoiceDocument;
import static ekol.orders.order.builder.MockOrderData.invoiceType;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DocumentValidatorUnitTest {

    private DocumentValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private DocumentTypeRepository documentTypeRepository;

    @Before
    public void init(){
        this.validator = new DocumentValidator(documentTypeRepository);

        given(documentTypeRepository.findById(1L)).willReturn(Optional.of(invoiceType().withId(1L).build()));
        given(documentTypeRepository.findById(2L)).willReturn(Optional.empty());

    }
    private void expectValidationException(List<Document> documents, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(documents);
    }
    @Test
    public void givenOrderWithEmptyDocument_whenValidateNew_thenThrowException() {
        expectValidationException(Collections.singletonList(null), "should have a type");
    }

    @Test
    public void givenOrderWithEmptyDocumentType_whenValidateNew_thenThrowException() {
        expectValidationException(Collections.singletonList(invoiceDocument().withType(null).build()), "should have a type");
    }

    @Test
    public void givenOrderWithEmptyDocumentId_whenValidateNew_thenThrowException() {
        expectValidationException(
                Collections.singletonList(invoiceDocument().withType(invoiceType().withId(null).build()).build()),
                "should have a type");
    }

    @Test
    public void givenOrderWithInvalidDocumentId_whenValidateNew_thenThrowException() {
        expectValidationException(
                Collections.singletonList(invoiceDocument().withType(invoiceType().withId(2L).build()).build()),
                "Document type with id 2 does not exist");
    }

    @Test
    public void givenOrderWithEmptySavedFileName_whenValidateNew_thenThrowException() {
        expectValidationException(
                Collections.singletonList(invoiceDocument().withSavedFileName(null).build()),
                "should have a file name");
    }

    @Test
    public void givenOrderWithEmptyOriginalFileName_whenValidateNew_thenThrowException() {
        expectValidationException(
                Collections.singletonList(invoiceDocument().withOriginalFileName(null).build()),
                "should have a file name");
    }
}
