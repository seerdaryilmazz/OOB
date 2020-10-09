package ekol.location.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.location.domain.CollectionRegion;
import ekol.location.domain.dto.DataForRegionMap;
import ekol.location.domain.dto.RegionOfLocationResponse;
import ekol.location.service.CollectionRegionService;
import ekol.location.service.OperationRegionService;
import ekol.location.util.ServiceUtils;

@RestController
@RequestMapping("/collection-region")
public class CollectionRegionController {

    @Autowired
    private OperationRegionService operationRegionService;

    @Autowired
    private CollectionRegionService collectionRegionService;

    @GetMapping("/map")
    public DataForRegionMap findDataForRegionMap(
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfOperationRegion", required = false) String commaSeparatedPolygonRegionIdsOfOperationRegion,
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfOtherCollectionRegions", required = false) String commaSeparatedPolygonRegionIdsOfOtherCollectionRegions,
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfSelectedCollectionRegion", required = false) String commaSeparatedPolygonRegionIdsOfSelectedCollectionRegion) {

        List<Long> polygonRegionIdsOfOperationRegion = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfOperationRegion);
        List<Long> polygonRegionIdsOfOtherCollectionRegions = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfOtherCollectionRegions);
        List<Long> polygonRegionIdsOfSelectedCollectionRegion = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfSelectedCollectionRegion);

        return operationRegionService.findDataForCollectionRegionMap(
                polygonRegionIdsOfOperationRegion, polygonRegionIdsOfOtherCollectionRegions, polygonRegionIdsOfSelectedCollectionRegion);
    }

    @GetMapping("/query-two/by-company-location")
    public RegionOfLocationResponse findThatContainsCompanyLocationAccordingToQueryTwo(@RequestParam(value = "companyLocationId") Long companyLocationId) {
        return collectionRegionService.findCollectionAndOperationRegionOfLocationId(companyLocationId);
    }
    
    @GetMapping("/query-two/by-customs-location")
    public RegionOfLocationResponse findThatContainsCustomsLocationAccordingToQueryTwo(@RequestParam(value = "customsLocationId") Long customsLocationId) {
    	return Optional.ofNullable(collectionRegionService.findPointCustomsLocationId(customsLocationId)).map(collectionRegionService::findCollectionAndOperationRegionByPoint).orElse(null);
    }

    @GetMapping("/query-two/{id}")
    public CollectionRegion findAccordingToQueryTwoById(@PathVariable Long id) {
        return collectionRegionService.findAccordingToQueryTwoByIdOrThrowResourceNotFoundException(id);
    }
}
