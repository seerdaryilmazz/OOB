package ekol.orders.order.controller;

import ekol.exceptions.ApplicationException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.resource.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/order/documents")
public class FileUploadController{

    private FileService fileService;

    @Autowired
    public FileUploadController(FileService fileService){
        this.fileService = fileService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadDocument(@RequestParam("file") MultipartFile document) {
        InputStream inputStream = null;
        try {
            inputStream = document.getInputStream();
            return fileService.saveFileToTempDir(inputStream, document.getOriginalFilename());
        } catch (IOException ioe) {
            throw new ApplicationException("Exception while trying to obtain document input stream", ioe);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @RequestMapping(value = "/download/{file:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadDocument(@PathVariable String file){
        if(!fileService.fileExistsInPermDir(file)){
            throw new ResourceNotFoundException("File {0} is not found", file);
        }
        Path filePath = fileService.getPermanentDirectory().resolve(file);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=\"" + filePath.getFileName() + "\"");
        headers.set("Content-Length", String.valueOf(filePath.toFile().length()));
        FileSystemResource fileSystemResource = new FileSystemResource(filePath.toFile());

        return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
    }
}
