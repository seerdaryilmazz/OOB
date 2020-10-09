package ekol.orders.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.orders.search.config.ShipmentsSearchConfig;
import ekol.orders.search.domain.ShipmentPartyResult;
import ekol.orders.search.domain.ShipmentsSearchResult;
import ekol.orders.search.service.ShipmentsSearchService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="/search")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ShipmentsSearchController {
	
	private ShipmentsSearchService shipmentsSearchService; 
	
	@Timed(value = "order.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
	@PostMapping("/shipments")
	public ShipmentsSearchResult searchShipments(@RequestParam(required=false, defaultValue=StringUtils.EMPTY) String q,  @RequestBody ShipmentsSearchConfig config) {
		return shipmentsSearchService.query(q, config);
	}
	
	@Timed(value = "order.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
	@GetMapping("/parties-by-template")
	public ShipmentPartyResult searchShipments(@RequestParam(required=true) String templateId) {
		return shipmentsSearchService.queryTopParties(templateId);
	}
}
