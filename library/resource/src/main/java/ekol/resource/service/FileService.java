package ekol.resource.service;

import ekol.exceptions.ApplicationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private static final int BUFFER_SIZE = 4096;

    // The colon at the end is there to give an empty value if the specified key can not be found.
    // If we omit the colon and the specified key can not be found, an exception occurs.
    @Value("${oneorder.temporaryDirectory:}")
    private String temporaryDirectoryParam;

    // The colon at the end is there to give an empty value if the specified key can not be found.
    // If we omit the colon and the specified key can not be found, an exception occurs.
    @Value("${oneorder.permanentDirectory:}")
    private String permanentDirectoryParam;

    private Path temporaryDirectory;

    private Path permanentDirectory;

    @PostConstruct
    public void init() {

        if (temporaryDirectoryParam == null || temporaryDirectoryParam.trim().length() == 0) {
            temporaryDirectoryParam = Paths.get(FileUtils.getTempDirectoryPath()).resolve("oneorderTempDir").toString();
        }

        LOGGER.debug("oneorderTempDir: " + temporaryDirectoryParam);

        temporaryDirectory = Paths.get(temporaryDirectoryParam).toAbsolutePath();

        if (permanentDirectoryParam == null || permanentDirectoryParam.trim().length() == 0) {
            permanentDirectoryParam = Paths.get(FileUtils.getUserDirectoryPath()).resolve("oneorderPermDir").toString();
        }

        LOGGER.debug("oneorderPermDir: " + permanentDirectoryParam);

        permanentDirectory = Paths.get(permanentDirectoryParam).toAbsolutePath();

        try {
            // If the directory exists, no exception is thrown.
            Files.createDirectories(temporaryDirectory);
        } catch (IOException e) {
            throw new ApplicationException("Directory could not be created: " + temporaryDirectory.toString(), e);
        }

        try {
            // If the directory exists, no exception is thrown.
            Files.createDirectories(permanentDirectory);
        } catch (IOException e) {
            throw new ApplicationException("Directory could not be created: " + permanentDirectory.toString(), e);
        }
    }

    public String saveFileToTempDir(InputStream is, String originalFilename) {

        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        Path filePath = temporaryDirectory.resolve(newFilename);

        // save file to temporary directory
        try {
            // This method does not close the input stream, it is chosen intentionally.
            FileUtils.copyToFile(is, filePath.toFile());
        } catch (IOException e) {
            throw new ApplicationException("File could not be saved: " + originalFilename, e);
        }

        return newFilename;
    }

    public void saveFile(InputStream is, String destinationDirectory, String filename) {
        saveFile(is, Paths.get(destinationDirectory), filename);
    }

    public void saveFile(InputStream is, Path destinationDirectory, String filename) {

        Path filePath = destinationDirectory.resolve(filename);

        try {
            // This method does not close the input stream, it is chosen intentionally.
            FileUtils.copyToFile(is, filePath.toFile());
        } catch (IOException e) {
            throw new ApplicationException("File could not be saved: " + filename, e);
        }
    }

    /**
     *
     * @return path of the directory that finally contains the file
     */
    public String moveFileToPermDir(String filename) {

        Path currentFilePath = temporaryDirectory.resolve(filename);
        Path newFilePath = permanentDirectory.resolve(filename);

        // move file to permanent directory
        try {
            FileUtils.moveFile(currentFilePath.toFile(), newFilePath.toFile());
        } catch (IOException e) {
            throw new ApplicationException("File could not be moved: " + filename, e);
        }

        return permanentDirectory.toString();
    }

    public void removeFileFromTempDir(String filename) {

        File file = temporaryDirectory.resolve(filename).toFile();

        if (file.exists()) {
            if (!FileUtils.deleteQuietly(file)) {
                throw new ApplicationException("File could not be deleted: " + filename);
            }
        } else {
            // do not throw exception if file does not exist
        }
    }

    public void removeFileFromPermDir(String filename) {

        File file = permanentDirectory.resolve(filename).toFile();

        if (file.exists()) {
            if (!FileUtils.deleteQuietly(file)) {
                throw new ApplicationException("File could not be deleted: " + filename);
            }
        } else {
            // do not throw exception if file does not exist
        }
    }

    public boolean fileExistsInTempDir(String filename) {
        return temporaryDirectory.resolve(filename).toFile().exists();
    }

    public boolean fileExistsInPermDir(String filename) {
        return permanentDirectory.resolve(filename).toFile().exists();
    }

    public Path getTemporaryDirectory() {
        return temporaryDirectory;
    }

    public Path getPermanentDirectory() {
        return permanentDirectory;
    }

    public void unzip(String zipFilePath, String destDirectory) throws IOException {

        File destDir = new File(destDirectory);

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = null;

        try {
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry;
            // iterates over entries in the zip file
            while ((entry = zipIn.getNextEntry()) != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, save it to a file
                    saveCurrentZipEntryToFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
            }
        } finally {
            if (zipIn != null) {
                try {
                    zipIn.close();
                } catch (IOException e) {
                    // do nothing because we cannot recover at this state
                }
            }
        }
    }

    private void saveCurrentZipEntryToFile(ZipInputStream zipIn, String filePath) throws IOException {

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] buffer = new byte[BUFFER_SIZE];
            int numOfBytesRead;
            while ((numOfBytesRead = zipIn.read(buffer)) != -1) {
                bos.write(buffer, 0, numOfBytesRead);
            }
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // do nothing because we cannot recover at this state
                }
            }
        }
    }
}
