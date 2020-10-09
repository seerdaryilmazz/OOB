package ekol.orders.order.service;

import ekol.exceptions.ApplicationException;
import ekol.orders.order.domain.Document;
import ekol.resource.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentFileService {

    private FileService fileService;

    @Autowired
    public DocumentFileService(FileService fileService){
        this.fileService = fileService;
    }

    public void moveDocumentsToPermanentSpace(List<Document> documents){
        //same file may be used more than once for different document types
        //and moving file to permanent space removes the file from temporary space
        //so we need to group documents by filename, and move each file to permanent space once.
        documents.stream()
                .collect(Collectors.groupingBy(Document::getSavedFileName))
                .entrySet().forEach(fileNameAndDocuments -> {
                    String permanentPath = moveFileToPermanentSpace(fileNameAndDocuments.getKey());
                    fileNameAndDocuments.getValue().forEach(orderDocument -> orderDocument.setPath(permanentPath));
                });
    }

    public void removeDocument(Document document){
        fileService.removeFileFromPermDir(document.getSavedFileName());
    }
    private String moveFileToPermanentSpace(String savedFileName){
        boolean fileExistsInPermanent = fileService.fileExistsInPermDir(savedFileName);
        boolean fileExistsInTemp = fileService.fileExistsInTempDir(savedFileName);
        if(fileExistsInPermanent){
            return fileService.getPermanentDirectory().resolve(savedFileName).toString();
        }

        if(!fileExistsInTemp){
            throw new ApplicationException("File {0} not found in temp directory", savedFileName);
        }

        return fileService.moveFileToPermDir(savedFileName);
    }
}
