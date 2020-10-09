package ekol.orders.search.domain;

import java.util.List;

import ekol.orders.search.config.ShipmentsSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentsSearchResult {
	
	private List<ShipmentSearchDocument> shipments;
    private long totalElements;
    private int totalPages;
    private ShipmentsSearchConfig config;
    private ShipmentSearchFilter filter =new ShipmentSearchFilter();
}
