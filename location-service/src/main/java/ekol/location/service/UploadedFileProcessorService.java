package ekol.location.service;

import ekol.resource.service.FileService;
import ekol.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
public class UploadedFileProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadedFileProcessorService.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private PolygonRegionService polygonRegionService;

    public static class UploadRequestResult {

        private boolean completed;
        private String message;

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void processUploadedFile(
            String shapesZipFileOriginalName,
            String directoryName,
            UploadRequestResult uploadRequestResult,
            boolean saveCoordinatesIndividually) {

        try {

            Path destinationDirectory = fileService.getTemporaryDirectory().resolve(directoryName);

            try {
                fileService.unzip(destinationDirectory.resolve(shapesZipFileOriginalName).toString(), destinationDirectory.toString());
                LOGGER.debug("Zip file unzipped");
            } catch (IOException e) {
                LOGGER.debug("Exception while unzipping zip file", e);
                throw new ApplicationException("Exception while unzipping zip file", e);
            }

            String countryIsoAlpha3Code = shapesZipFileOriginalName.substring(0, 3);

            Set<Integer> levels = new HashSet<>();

            for (File f : destinationDirectory.toFile().listFiles()) {
                if (f.isFile() && f.getName().startsWith(countryIsoAlpha3Code) && f.getName().endsWith(".shp")) {
                    levels.add(Integer.parseInt(f.getName().substring(7, 8)));
                }
            }

            for (Integer level : levels) {
                polygonRegionService.deleteExistingPolygonRegions(countryIsoAlpha3Code, level);
            }
            LOGGER.debug("Existing polygon regions deleted");

            for (Integer level : levels) {
                polygonRegionService.processShapeFile(
                        destinationDirectory.toString(), countryIsoAlpha3Code, level, saveCoordinatesIndividually);
            }
            LOGGER.debug("Shape files processed");

            uploadRequestResult.setCompleted(true);
            uploadRequestResult.setMessage("Completed successfully.");

        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            uploadRequestResult.setCompleted(true);
            uploadRequestResult.setMessage(getStackTraceAsString(e));
        }
    }

    private static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
