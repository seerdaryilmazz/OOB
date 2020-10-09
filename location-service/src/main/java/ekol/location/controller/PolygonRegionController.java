package ekol.location.controller;

import ekol.resource.service.FileService;
import ekol.exceptions.ApplicationException;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.Country;
import ekol.location.domain.PolygonRegion;
import ekol.location.repository.CountryRepository;
import ekol.location.repository.PolygonRegionRepository;
import ekol.location.service.PolygonRegionService;
import ekol.location.service.UploadedFileProcessorService;
import ekol.location.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/polygon-region")
public class PolygonRegionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonRegionController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    @Autowired
    private UploadedFileProcessorService uploadedFileProcessorService;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @Autowired
    private CountryRepository countryRepository;

    // TODO: Bu MAP nasıl boşaltılacak?
    // key: upload request id
    // value: upload request result
    private static final Map<String, UploadedFileProcessorService.UploadRequestResult> UPLOAD_REQUEST_RESULT_MAP = new HashMap<>();

    @RequestMapping(
            value = {"/upload", "/upload/"},
            method = RequestMethod.POST)
    public String upload(@RequestParam("shapesZipFile") MultipartFile shapesZipFile,
                         @RequestParam("saveCoordinatesIndividually") boolean saveCoordinatesIndividually) {

        String uploadRequestId = UUID.randomUUID().toString();
        UploadedFileProcessorService.UploadRequestResult uploadRequestResult = new UploadedFileProcessorService.UploadRequestResult();

        UPLOAD_REQUEST_RESULT_MAP.put(uploadRequestId, uploadRequestResult);

        // uploadRequestId'yi aynı zamanda geçici bir dizin olarak kullanıyoruz.
        String directoryName = uploadRequestId;

        Path destinationDirectory = fileService.getTemporaryDirectory().resolve(directoryName);

        LOGGER.debug("destinationDirectory: " + destinationDirectory);

        try {
            Files.createDirectories(destinationDirectory);
        } catch (IOException e) {
            throw new ApplicationException("Exception while creating directory: " + destinationDirectory.toString(), e);
        }

        try {
            fileService.saveFile(shapesZipFile.getInputStream(), destinationDirectory, shapesZipFile.getOriginalFilename());
            LOGGER.debug("Zip file saved");
        } catch (IOException e) {
            LOGGER.debug("Exception while saving zip file", e);
            throw new ApplicationException("Exception while saving zip file", e);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                uploadedFileProcessorService.processUploadedFile(
                        shapesZipFile.getOriginalFilename(), uploadRequestId, uploadRequestResult, saveCoordinatesIndividually);
            }
        };

        new Thread(runnable).start();

        return uploadRequestId;
    }

    @RequestMapping(value = "/upload-req-result/{uploadRequestId}", method = RequestMethod.GET)
    public String queryUploadRequestResult(@PathVariable String uploadRequestId) {

        UploadedFileProcessorService.UploadRequestResult result = UPLOAD_REQUEST_RESULT_MAP.get(uploadRequestId);

        if (result == null) {
            throw new BadRequestException("Invalid upload request id: " + uploadRequestId);
        } else {
            if (result.isCompleted()) {
                return result.getMessage();
            } else {
                return "In progress";
            }
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PolygonRegion find(@PathVariable Long id) {
        return polygonRegionService.findForDrawingByIdOrThrowResourceNotFoundException(id);
    }

    @RequestMapping(value = "/by-ids", method = RequestMethod.GET)
    public List<PolygonRegion> findByIds(@RequestParam("ids") String ids) {
        return polygonRegionService.findPolygonRegionsByIds(ServiceUtils.commaSeparatedNumbersToLongList(ids));
    }

    @RequestMapping(value = "/by-country", method = RequestMethod.GET)
    public PolygonRegion findByCountry(@RequestParam("isoAlpha3Code") String isoAlpha3Code) {
        return polygonRegionService.findLevel0PolygonRegionByCountryIsoAlpha3Code(isoAlpha3Code);
    }

    @RequestMapping(value = "/children", method = RequestMethod.GET)
    public List<PolygonRegion> findChildren(@RequestParam("parent") String parent, @RequestParam("name") String name) {

        PolygonRegion polygonRegion = polygonRegionRepository.findByParentAndName(parent, name);

        if (polygonRegion == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        if (!polygonRegion.getHasChildren()) {
            throw new BadRequestException("This polygon region does not have children.");
        } else {
            return polygonRegionService.findPolygonRegionsByParent(polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion));
        }
    }

    @RequestMapping(value = "/ids-of-children", method = RequestMethod.GET)
    public List<Long> findIdsOfChildren(@RequestParam("parent") String parent, @RequestParam("name") String name) {

        PolygonRegion polygonRegion = polygonRegionRepository.findByParentAndName(parent, name);

        if (polygonRegion == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        if (!polygonRegion.getHasChildren()) {
            throw new BadRequestException("This polygon region does not have children.");
        } else {
            return polygonRegionService.findIdsOfPolygonRegionsByParent(polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion));
        }
    }

    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public PolygonRegion findParent(@RequestParam("parent") String parent, @RequestParam("name") String name) {

        PolygonRegion polygonRegion = polygonRegionRepository.findByParentAndName(parent, name);

        if (polygonRegion == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        if (!polygonRegion.getHasParent()) {
            throw new BadRequestException("This polygon region does not have a parent.");
        } else {
            String[] queryParams = polygonRegionService.getQueryParamsForParentPolygonRegionQuery(polygonRegion);
            String queryParamParent = queryParams[0];
            String queryParamName = queryParams[1];
            return polygonRegionService.findPolygonRegionByParentAndName(queryParamParent, queryParamName);
        }
    }

    @RequestMapping(value = "/id-of-parent", method = RequestMethod.GET)
    public Long findIdOfParent(@RequestParam("parent") String parent, @RequestParam("name") String name) {

        PolygonRegion polygonRegion = polygonRegionRepository.findByParentAndName(parent, name);

        if (polygonRegion == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        if (!polygonRegion.getHasParent()) {
            throw new BadRequestException("This polygon region does not have a parent.");
        } else {
            String[] queryParams = polygonRegionService.getQueryParamsForParentPolygonRegionQuery(polygonRegion);
            String queryParamParent = queryParams[0];
            String queryParamName = queryParams[1];
            return polygonRegionService.findIdOfPolygonRegionByParentAndName(queryParamParent, queryParamName);
        }
    }

    @RequestMapping(value = "/by-postcode", method = RequestMethod.GET)
    public List<PolygonRegion> findByPostcode(
            @RequestParam("isoAlpha3Code") String isoAlpha3Code,
            @RequestParam(value = "postcodeEquals", required = false) String postcodeEquals,
            @RequestParam(value = "postcodeStartsWith", required = false) String postcodeStartsWith,
            @RequestParam(value = "searchForDrawing") boolean searchForDrawing) {

        Country country = countryRepository.findByIsoAlpha3Code(isoAlpha3Code);

        if (country == null) {
            throw new ResourceNotFoundException("Country with specified iso alpha 3 code cannot be found: " + isoAlpha3Code);
        }

        List<PolygonRegion> polygonRegionList;

        if (postcodeEquals != null && postcodeEquals.length() > 0) {
            polygonRegionList = polygonRegionService.findPolygonRegionsByPostcode(country, postcodeEquals, searchForDrawing);
        } else if (postcodeStartsWith != null && postcodeStartsWith.length() > 0) {
            polygonRegionList = polygonRegionService.findPolygonRegionsByPostcodeStartsWith(country, postcodeStartsWith, searchForDrawing);
        } else {
            throw new BadRequestException("Check query parameters");
        }

        return polygonRegionList;
    }
}
