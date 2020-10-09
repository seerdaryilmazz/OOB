package ekol.orders.transportOrder.elastic.shipment.model;

import ekol.orders.transportOrder.elastic.shipment.config.ShipmentSearchConfig;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozer on 03/10/16.
 */
public class ShipmentSearchResult {

    private List<List<ShipmentDocument>> shipments = new ArrayList<>();
    private List<Bucket> buckets;
    private long totalElements;
    private int totalPages;
    private ShipmentSearchConfig config;
    private RangeType[] rangeTypes = RangeType.values();
    private SortType[] sortTypes = SortType.values();
    private GroupType[] groupTypes = GroupType.values();
    private MatchType[] matchTypes = MatchType.values();

    public List<List<ShipmentDocument>> getShipments() {
        return shipments;
    }

    public void setShipments(List<List<ShipmentDocument>> shipments) {
        this.shipments = shipments;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public ShipmentSearchConfig getConfig() {
        return config;
    }

    public void setConfig(ShipmentSearchConfig config) {
        this.config = config;
    }

    public RangeType[] getRangeTypes() {
        return rangeTypes;
    }

    public void setRangeTypes(RangeType[] rangeTypes) {
        this.rangeTypes = rangeTypes;
    }

    public SortType[] getSortTypes() {
        return sortTypes;
    }

    public void setSortTypes(SortType[] sortTypes) {
        this.sortTypes = sortTypes;
    }

    public GroupType[] getGroupTypes() {
        return groupTypes;
    }

    public void setGroupTypes(GroupType[] groupTypes) {
        this.groupTypes = groupTypes;
    }

    public MatchType[] getMatchTypes() {
        return matchTypes;
    }

    public void setMatchTypes(MatchType[] matchTypes) {
        this.matchTypes = matchTypes;
    }
}
