package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.location.domain.RouteLegExpedition;
import ekol.location.domain.RouteLegExpeditionTrip;
import ekol.location.domain.dto.TripPlanEventMessage;
import ekol.location.repository.RouteLegExpeditionTripRepository;
import ekol.model.IdNamePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by burak on 15/12/17.
 */
@Service
public class RouteLegExpeditionTripService {

    @Autowired
    private RouteLegExpeditionTripRepository routeLegExpeditionTripRepository;

    @Autowired
    private RouteLegExpeditionService routeLegExpeditionService;

    @Transient
    public void updateExpeditions(TripPlanEventMessage message) {

        TripPlanEventMessage.Trip lastTrip = message.getTrips().get(message.getTrips().size()-1);

        message.getTrips().forEach(trip -> {
            if(trip.getFromTripStop().getRoute() != null) {
                trip.getFromTripStop().getRoute().getRouteLegs().stream().forEach(routeLeg -> {

                    if(routeLeg.getExpeditionId() != null) {
                        RouteLegExpeditionTrip routeLegExpeditionTrip = routeLegExpeditionTripRepository.findByExpeditionIdAndTripId(routeLeg.getExpeditionId(), trip.getId());

                        if (routeLegExpeditionTrip == null) {

                            routeLegExpeditionTrip = new RouteLegExpeditionTrip();

                            routeLegExpeditionTrip.setTripId(trip.getId());
                            routeLegExpeditionTrip.setTripPlanCode(message.getCode());

                            if(trip.getVehicleGroup() != null) {
                                IdNamePair trailable = trip.getVehicleGroup().getTrailable();
                                if (trailable != null) {
                                    routeLegExpeditionTrip.setTrailerId(trailable.getId());
                                    routeLegExpeditionTrip.setTrailerPlate(trailable.getName());
                                }
                            }

                            if(trip.getSpotVehicle() != null) {
                                TripPlanEventMessage.SpotVehicle spotVehicle = trip.getSpotVehicle();
                                routeLegExpeditionTrip.setSpotVehicleId(spotVehicle.getVehicleId());
                                routeLegExpeditionTrip.setSpotVehicleRouteId(spotVehicle.getVehicleRouteId());
                                routeLegExpeditionTrip.setSpotVehicleCarrierPlate(spotVehicle.getCarrierPlateNumber());
                            }

                            RouteLegExpedition expedition = routeLegExpeditionService.findById(routeLeg.getExpeditionId());
                            routeLegExpeditionTrip.setExpedition(expedition);

                            routeLegExpeditionTrip.setNextLocationId(trip.getToTripStop().getLocation().getId());
                            routeLegExpeditionTrip.setNextLocationName(trip.getToTripStop().getLocation().getName());
                            routeLegExpeditionTrip.setNextLocationPostalCode(trip.getToTripStop().getLocation().getPostalCode());
                            routeLegExpeditionTrip.setNextLocationCountryCode(trip.getToTripStop().getLocation().getCountryCode());
                            routeLegExpeditionTrip.setFinalLocationCountryCode(lastTrip.getToTripStop().getLocation().getCountryCode());
                            routeLegExpeditionTrip.setNextLocationEta(
                                    new FixedZoneDateTime(
                                    lastTrip.getToTripStop().getEstimatedTimeArrival().getDateTime(),
                                    lastTrip.getToTripStop().getEstimatedTimeArrival().getTimezone())
                            );

                            routeLegExpeditionTripRepository.save(routeLegExpeditionTrip);

                            expedition.setCapacityUsage(routeLegExpeditionTripRepository.countByExpeditionId(expedition.getId()));
                            routeLegExpeditionService.update(expedition);
                        } else {
                            //TODO: what to do if the entry is already created before and this is the sme requrest or update request?
                        }

                    }
                });
            }
        });
    }

    public List<RouteLegExpeditionTrip> findByExpeditionId(Long expeditionId) {

        if(expeditionId == null) {
            throw new BadRequestException("Expedition id is null.");
        }

        return routeLegExpeditionTripRepository.findAllByExpeditionId(expeditionId);
    }
}
