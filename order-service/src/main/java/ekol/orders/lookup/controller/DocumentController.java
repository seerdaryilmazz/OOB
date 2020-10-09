package ekol.orders.lookup.controller;


import ekol.resource.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class DocumentController extends ekol.hibernate5.domain.controller.DocumentApiController {

    @Autowired
    private FileService fileService;

    public FileService getFileService() {
        return fileService;
    }
}
