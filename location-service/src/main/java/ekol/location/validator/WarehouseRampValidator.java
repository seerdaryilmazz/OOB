package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.domain.location.warehouse.WarehouseZone;
import ekol.location.domain.location.warehouse.dto.WarehouseRampGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by burak on 08/02/17.
 */
public class WarehouseRampValidator {


    public static void validate(Warehouse warehouse) {

        Set<Integer> rampNos = new HashSet<>();

        warehouse.getRampGroup().forEach(ramp -> {

            if (ramp.getRampFrom() == null || ramp.getRampTo() == null) {
                throw new BadRequestException("Dock No can not be null");
            }

            if (ramp.getProperty() == null) {
                throw new BadRequestException("Dock Property can not be null or empty");
            }

            validateRangeValues(ramp, warehouse.getRampGroup());

            if (warehouse.getNumberOfFloors() != null && ramp.getFloorNumber() != null && warehouse.getNumberOfFloors().compareTo(ramp.getFloorNumber()) < 0) {
                throw new ValidationException("Dock '" + ramp.retrieveRampNumberIntervalAsString() + "' floor number '" + ramp.getFloorNumber() + "'"
                        + " can not be greater than Warehouse # of Floors (" + warehouse.getNumberOfFloors().intValue() + ")");
            }

            for (int i = ramp.getRampFrom(); i <= ramp.getRampTo(); i++) {
                rampNos.add(i);
            }

        });

        if (warehouse.getNumberOfRamps() != null && warehouse.getNumberOfRamps().compareTo(rampNos.size()) < 0) {
            throw new ValidationException("Total number of defined docks(" + rampNos.size() + ")" +
                    " exceeds the number of docks in warehouse definition("+ warehouse.getNumberOfRamps() + ")");
        }

        validateUsedRampsNotRemoved(warehouse.getZone(), rampNos);

    }

    public static void validateUsedRampsNotRemoved(Set<WarehouseZone> zones, Set<Integer> rampNos) {
        zones.stream().forEach(zone -> {
            if( zone.getGoodsInRamps() != null) {
                zone.getGoodsInRamps().forEach(r -> {
                    if (!rampNos.contains(r.getRampNo())) {
                        throw new ValidationException("Dock No '" + r.getRampNo() + "' is  used by Zone '" + zone.getName() + "'" +
                                ", therefore, it can not be removed.");
                    }
                });
            }
            if( zone.getGoodsOutRamps() != null) {

                zone.getGoodsOutRamps().forEach(r -> {
                    if (!rampNos.contains(r.getRampNo())) {
                        throw new ValidationException("Dock No '" + r.getRampNo() + "' is  used by Zone '" + zone.getName() + "'" +
                                ", therefore, it can not be removed.");
                    }
                });
            }
        });

    }


    public static void validateRangeValues(WarehouseRampGroup ramp, Set<WarehouseRampGroup> whRampList) {
        if (ramp.getRampFrom().compareTo(ramp.getRampTo()) > 0) {
            throw new ValidationException("Dock Definition: Dock From value can not be greater than Dock To value for the Dock: " + ramp.retrieveRampNumberIntervalAsString());
        }

        whRampList.forEach(r -> {
            if (!r.equals(ramp) && r.getRampFrom().compareTo(ramp.getRampTo()) <= 0 && r.getRampTo().compareTo(ramp.getRampFrom()) >= 0) {
                throw new ValidationException("There is a confliction between Dock '" + ramp.retrieveRampNumberIntervalAsString()
                        + "' and '" + r.retrieveRampNumberIntervalAsString() + "'");
            }
        });
    }
}
