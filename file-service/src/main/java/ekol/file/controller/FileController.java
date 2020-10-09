package ekol.file.controller;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ekol.exceptions.*;
import ekol.file.domain.FileEntry;
import ekol.file.repository.FileEntryRepository;
import ekol.file.utils.MimeTypeUtils;

@RestController
public class FileController {

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Value("${oneorder.fileRepoPath:}")
    private String fileRepoPathString;

    private Path fileRepoPath;

    @Autowired
    private FileEntryRepository fileEntryRepository;
    
    @Autowired
    private MimeTypeUtils mimeTypeUtils;

    @PostConstruct
    public void init() {

        if (StringUtils.isEmpty(fileRepoPathString)) {
            throw new ApplicationException("Property is not found: fileRepoPath");
        }

        fileRepoPath = Paths.get(fileRepoPathString);

        if (!Files.exists(fileRepoPath) || !Files.isDirectory(fileRepoPath)) {
            throw new ApplicationException("Directory does not exist: " + fileRepoPathString);
        }

        System.out.println("fileRepoPath: " + fileRepoPathString);
    }

    @PostMapping({"/upload", "/upload/"})
    public FileEntry upload(@RequestParam("file") MultipartFile multipartFile) {

        InputStream inputStream = null;

        try {

            String originalFilename = FilenameUtils.getName(multipartFile.getOriginalFilename());
            if(100 < StringUtils.length(originalFilename)) {
            	throw new ValidationException("File names can be 100 characters long as maximum!");
            }
            String extension = FilenameUtils.getExtension(originalFilename);
            String physicalFilename;

            if (StringUtils.isNotEmpty(extension)) {
                physicalFilename = FilenameUtils.getBaseName(originalFilename) + "_" + UUID.randomUUID().toString() + "." + extension;
            } else {
                physicalFilename = originalFilename + "_" + UUID.randomUUID().toString();
            }

            ZonedDateTime utcDateTime = ZonedDateTime.now(UTC);

            FileEntry fileEntry = new FileEntry();
            fileEntry.setId(UUID.randomUUID().toString());
            fileEntry.setName(originalFilename);
            fileEntry.setPhysicalName(physicalFilename);
            fileEntry.setUploadDateTime(utcDateTime.toLocalDateTime());
            fileEntryRepository.save(fileEntry);

            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be opened.", e);
            }

            try {
                FileUtils.copyToFile(inputStream, fileRepoPath.resolve(physicalFilename).toFile());
            } catch (IOException e) {
                throw new ApplicationException("File could not be uploaded: " + originalFilename, e);
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be closed.", e);
            }

            return fileEntry;

        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @GetMapping("/{id}")
    public FileEntry find(@PathVariable String id) {

        FileEntry fileEntry = fileEntryRepository.findOne(id);

        if (fileEntry == null) {
            throw new ResourceNotFoundException("File not found. id: {0}", id);
        }

        return fileEntry;
    }

    @GetMapping("/{id}/download")
    public void download(HttpServletResponse response, @PathVariable String id, @RequestParam(defaultValue = "attachment") String contentDisposition) {

        FileEntry fileEntry = fileEntryRepository.findOne(id);

        if (fileEntry == null) {
            throw new ResourceNotFoundException("File not found. id: {0}", id);
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            response.setContentType(mimeTypeUtils.guessContentType(fileEntry.getName()));
            response.setHeader("Content-Disposition", String.format("%s; filename=\"%s\"", contentDisposition, fileEntry.getName()));

            try {
                inputStream = new BufferedInputStream(new FileInputStream(fileRepoPath.resolve(fileEntry.getPhysicalName()).toFile()));
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be opened.", e);
            }

            try {
                outputStream = response.getOutputStream();
            } catch (IOException e) {
                throw new ApplicationException("Output stream could not be opened.", e);
            }

            byte[] buffer = new byte[8192];
            int numberOfBytesRead;

            try {
                while ((numberOfBytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, numberOfBytesRead);
                }
            } catch (IOException e) {
                throw new ApplicationException("Exception while transferring bytes", e);
            }

            try {
                outputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("Output stream could not be closed.", e);
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be closed.", e);
            }

        } catch (Exception e) {
            throw new ApplicationException("Could not download file", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }
}
