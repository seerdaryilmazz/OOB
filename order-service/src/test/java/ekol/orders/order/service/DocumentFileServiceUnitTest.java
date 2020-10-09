package ekol.orders.order.service;

import ekol.exceptions.ApplicationException;
import ekol.orders.order.builder.DocumentBuilder;
import ekol.orders.order.domain.Document;
import ekol.resource.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class DocumentFileServiceUnitTest {

    @MockBean
    private FileService fileService;

    private DocumentFileService documentFileService;

    @Before
    public void init() {
        this.documentFileService = new DocumentFileService(fileService);
    }


    @Test
    public void givenDocumentsExistsInTempSpaceButNotInPermSpace_whenMoveToPermSpace_thenMoveDocumentsAndSetPath() {
        List<Document> documents = Arrays.asList(
            DocumentBuilder.aDocument().withSavedFileName("file-1-saved").withOriginalFileName("org-file-1").build(),
            DocumentBuilder.aDocument().withSavedFileName("file-2-saved").withOriginalFileName("org-file-2").build()
        );

        given(fileService.moveFileToPermDir("file-1-saved")).willReturn("/path/to/file-1-saved");
        given(fileService.moveFileToPermDir("file-2-saved")).willReturn("/path/to/file-2-saved");

        given(fileService.fileExistsInPermDir("file-1-saved")).willReturn(false);
        given(fileService.fileExistsInPermDir("file-2-saved")).willReturn(false);
        given(fileService.fileExistsInTempDir("file-1-saved")).willReturn(true);
        given(fileService.fileExistsInTempDir("file-2-saved")).willReturn(true);

        documentFileService.moveDocumentsToPermanentSpace(documents);

        assertEquals("/path/to/file-1-saved", documents.get(0).getPath());
        assertEquals("/path/to/file-2-saved", documents.get(1).getPath());

        then(fileService).should(times(1)).moveFileToPermDir("file-1-saved");
        then(fileService).should(times(1)).moveFileToPermDir("file-2-saved");
    }

    @Test
    public void givenDocumentsNotExistsInTempSpaceButInPermSpace_whenMoveToPermSpace_thenSetPathOnly() {
        List<Document> documents = Arrays.asList(
                DocumentBuilder.aDocument().withSavedFileName("file-1-saved").withOriginalFileName("org-file-1").build(),
                DocumentBuilder.aDocument().withSavedFileName("file-2-saved").withOriginalFileName("org-file-2").build()
        );

        given(fileService.getPermanentDirectory()).willReturn(Paths.get("/path/to"));

        given(fileService.fileExistsInPermDir("file-1-saved")).willReturn(true);
        given(fileService.fileExistsInPermDir("file-2-saved")).willReturn(true);
        given(fileService.fileExistsInTempDir("file-1-saved")).willReturn(false);
        given(fileService.fileExistsInTempDir("file-2-saved")).willReturn(false);

        documentFileService.moveDocumentsToPermanentSpace(documents);

        assertEquals("/path/to/file-1-saved", documents.get(0).getPath());
        assertEquals("/path/to/file-2-saved", documents.get(1).getPath());

        then(fileService).should(never()).moveFileToPermDir("file-1-saved");
        then(fileService).should(never()).moveFileToPermDir("file-2-saved");
    }
    @Test(expected = ApplicationException.class)
    public void givenDocumentsNotExistsInTempSpaceAndInPermSpace_whenMoveToPermSpace_thenThrowException() {
        List<Document> documents = Arrays.asList(
                DocumentBuilder.aDocument().withSavedFileName("file-1-saved").withOriginalFileName("org-file-1").build(),
                DocumentBuilder.aDocument().withSavedFileName("file-2-saved").withOriginalFileName("org-file-2").build()
        );

        given(fileService.fileExistsInPermDir("file-1-saved")).willReturn(false);
        given(fileService.fileExistsInPermDir("file-2-saved")).willReturn(false);
        given(fileService.fileExistsInTempDir("file-1-saved")).willReturn(false);
        given(fileService.fileExistsInTempDir("file-2-saved")).willReturn(false);

        documentFileService.moveDocumentsToPermanentSpace(documents);

    }

    @Test
    public void givenDocument_whenRemoveDocument_thenRemoveDocument() {
        Document document = DocumentBuilder.aDocument().withSavedFileName("file-1-saved").withOriginalFileName("org-file-1").build();

        documentFileService.removeDocument(document);

        then(fileService).should(times(1)).removeFileFromPermDir(document.getSavedFileName());

    }
}
