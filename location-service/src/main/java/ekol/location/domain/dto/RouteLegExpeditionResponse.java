package ekol.location.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.location.domain.RouteLegExpedition;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteLegExpeditionResponse {

    public Long id;
    private FixedZoneDateTime departure;
    private FixedZoneDateTime arrival;

    public static RouteLegExpeditionResponse with(RouteLegExpedition expedition) {
        RouteLegExpeditionResponse response = new RouteLegExpeditionResponse();

        response.setId(expedition.getId());
        response.setDeparture(expedition.getDeparture());
        response.setArrival(expedition.getArrival());

        return response;
    }

    public static List<RouteLegExpeditionResponse> withCollection(List<RouteLegExpedition> expeditions) {

        if (expeditions == null) return null;

        List<RouteLegExpeditionResponse> response = new ArrayList<>();
        expeditions.forEach(expedition -> {
            response.add(RouteLegExpeditionResponse.with(expedition));
        });

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FixedZoneDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(FixedZoneDateTime departure) {
        this.departure = departure;
    }

    public FixedZoneDateTime getArrival() {
        return arrival;
    }

    public void setArrival(FixedZoneDateTime arrival) {
        this.arrival = arrival;
    }
}