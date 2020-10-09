package ekol.orders.transportOrder.elastic.shipment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.orders.transportOrder.elastic.shipment.config.ShipmentSearchConfig;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.model.ShipmentSearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozer on 03/10/16.
 */
@Service
public class ShipmentSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public ShipmentSearchResult searchShipments(ShipmentSearchConfig config) {
        ShipmentSearchManager shipmentSearchManager = new ShipmentSearchManager(config, elasticsearchTemplate);
        return shipmentSearchManager.getSearchResult();
    }


    public ShipmentDocument searchShipmentByShipmentId(Long shipmentId) {
        if (!elasticsearchTemplate.indexExists(ShipmentDocument.class)) {
            return null;
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(ShipmentDocument.INDEX_NAME).withQuery(
                QueryBuilders.matchQuery("id", String.valueOf(shipmentId))).build();

        return elasticsearchTemplate.query(query, response -> {

            if (response.getHits() != null && response.getHits().getTotalHits() > 0) {
                List<Exception> exceptions = new ArrayList<>();
                try {
                    return new ObjectMapper().readValue(response.getHits().getAt(0).getSourceAsString(), ShipmentDocument.class);
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        });
    }

    public List<ShipmentDocument> searchShipmentsByTransportId(Long transportOrderId) {
        if (!elasticsearchTemplate.indexExists(ShipmentDocument.class)) {
            return null;
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(ShipmentDocument.INDEX_NAME).withQuery(
                QueryBuilders.matchQuery("transportOrderId", String.valueOf(transportOrderId))).build();

        return elasticsearchTemplate.queryForList(query, ShipmentDocument.class);
    }

    public List<ShipmentDocument> searchShipmentsBySubsidiaryId(Long subsidiaryId) {
        if (!elasticsearchTemplate.indexExists(ShipmentDocument.class)) {
            return null;
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(ShipmentDocument.INDEX_NAME).withQuery(
                QueryBuilders.nestedQuery("subsidiary", QueryBuilders.matchQuery("subsidiary.id", String.valueOf(subsidiaryId)))
        ).withPageable(new PageRequest(0, 100)).build();


        return elasticsearchTemplate.queryForList(query, ShipmentDocument.class);
    }
}
