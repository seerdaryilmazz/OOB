package ekol.orders.transportOrder.elastic.shipment.config;

/**
 * Created by ozer on 05/10/16.
 */
public class RangeFilter {

    private String name;
    private String gte;
    private String lte;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGte() {
        return gte;
    }

    public void setGte(String gte) {
        this.gte = gte;
    }

    public String getLte() {
        return lte;
    }

    public void setLte(String lte) {
        this.lte = lte;
    }
}
