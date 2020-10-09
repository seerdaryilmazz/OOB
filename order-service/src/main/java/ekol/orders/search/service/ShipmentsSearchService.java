package ekol.orders.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.orders.search.config.ShipmentsSearchConfig;
import ekol.orders.search.domain.ShipmentPartyResult;
import ekol.orders.search.domain.ShipmentsSearchResult;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ShipmentsSearchService {

	ShipmentsSearchManager shipmentsSearchManager; 
	ShipmentTopPartiesManager shipmentTopPartiesManager;

	public ShipmentsSearchResult query(String q, ShipmentsSearchConfig config){
		return shipmentsSearchManager.setConfig(config).getSearchResult(q);
	}

	public ShipmentPartyResult queryTopParties(String templateId) {
		return shipmentTopPartiesManager.query(templateId);
	}

}
