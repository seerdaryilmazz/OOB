package ekol.location.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.location.domain.DistributionRegion;
import ekol.location.domain.dto.DataForRegionMap;
import ekol.location.domain.dto.RegionOfLocationResponse;
import ekol.location.service.DistributionRegionService;
import ekol.location.service.OperationRegionService;
import ekol.location.util.ServiceUtils;

@RestController
@RequestMapping("/distribution-region")
public class DistributionRegionController {

    @Autowired
    private OperationRegionService operationRegionService;

    @Autowired
    private DistributionRegionService distributionRegionService;

    @GetMapping("/map")
    public DataForRegionMap findDataForRegionMap(
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfOperationRegion", required = false) String commaSeparatedPolygonRegionIdsOfOperationRegion,
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfOtherDistributionRegions", required = false) String commaSeparatedPolygonRegionIdsOfOtherDistributionRegions,
            @RequestParam(value = "commaSeparatedPolygonRegionIdsOfSelectedDistributionRegion", required = false) String commaSeparatedPolygonRegionIdsOfSelectedDistributionRegion) {

        List<Long> polygonRegionIdsOfOperationRegion = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfOperationRegion);
        List<Long> polygonRegionIdsOfOtherDistributionRegions = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfOtherDistributionRegions);
        List<Long> polygonRegionIdsOfSelectedDistributionRegion = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedPolygonRegionIdsOfSelectedDistributionRegion);

        return operationRegionService.findDataForDistributionRegionMap(
                polygonRegionIdsOfOperationRegion, polygonRegionIdsOfOtherDistributionRegions, polygonRegionIdsOfSelectedDistributionRegion);
    }
    
    @GetMapping("/query-two/by-company-location")
    public RegionOfLocationResponse findThatContainsCompanyLocationAccordingToQueryTwo(@RequestParam(value = "companyLocationId") Long companyLocationId) {
    	return distributionRegionService.findDistributionAndOperationRegionOfLocationId(companyLocationId);
    }

    @GetMapping("/query-two/by-customs-location")
    public RegionOfLocationResponse findThatContainsCustomsLocationAccordingToQueryTwo(@RequestParam(value = "customsLocationId") Long customsLocationId) {
        return Optional.ofNullable(distributionRegionService.findPointCustomsLocationId(customsLocationId)).map(distributionRegionService::findDistributionAndOperationRegionByPoint).orElse(null);
    }

    @GetMapping("/query-two/{id}")
    public DistributionRegion findAccordingToQueryTwoById(@PathVariable Long id) {
        return distributionRegionService.findAccordingToQueryTwoByIdOrThrowResourceNotFoundException(id);
    }
}
