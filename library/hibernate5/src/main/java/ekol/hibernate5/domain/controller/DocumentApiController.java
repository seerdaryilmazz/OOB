package ekol.hibernate5.domain.controller;

import ekol.resource.service.FileService;
import ekol.exceptions.ApplicationException;
import ekol.hibernate5.domain.entity.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public abstract class DocumentApiController {

    public abstract FileService getFileService();

    @RequestMapping(
            value = {"/upload", "/upload/"},
            method = RequestMethod.POST)
    public Document uploadDocument(@RequestParam("document") MultipartFile document) {

        InputStream inputStream = null;

        try {
            inputStream = document.getInputStream();
        } catch (IOException e) {
            throw new ApplicationException("Exception while trying to obtain document input stream", e);
        }

        String newName = null;

        try {
            newName = getFileService().saveFileToTempDir(inputStream, document.getOriginalFilename());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Hiçbir şey yapma.
                }
            }
        }

        Document documentEntity = new Document();
        documentEntity.setOriginalName(document.getOriginalFilename());
        documentEntity.setDisplayName(document.getOriginalFilename()); // 'displayName' sonradan kullanıcı tarafından değiştirilebilir.
        documentEntity.setFileName(newName); // 'newName' eşsizdir.
        documentEntity.setDirectoryPath(null); // Client'ın bunu bilmesine gerek yok, biz istediğimiz zaman bu bilgiye fileName'i kullanarak ulaşabiliyoruz.

        return documentEntity;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadDocument(
            HttpServletResponse response,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileNameClient") String fileNameClient) {

        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileNameClient));

            if (getFileService().fileExistsInPermDir(fileName)) {
                file = getFileService().getPermanentDirectory().resolve(fileName).toFile();
            } else {
                file = getFileService().getTemporaryDirectory().resolve(fileName).toFile();
            }

            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
            } catch (IOException e) {
                throw new ApplicationException("Exception while trying to obtain document input stream", e);
            }

            try {
                outputStream = response.getOutputStream();
            } catch (IOException e) {
                throw new ApplicationException("Exception while trying to obtain response output stream", e);
            }

            byte[] buffer = new byte[4096];
            int numberOfBytesRead = -1;

            try {
                while ((numberOfBytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, numberOfBytesRead);
                }
            } catch (IOException e) {
                throw new ApplicationException("Exception while reading from input stream or writing to output stream", e);
            }

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Hiçbir şey yapma.
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Hiçbir şey yapma.
                }
            }
        }
    }
}
