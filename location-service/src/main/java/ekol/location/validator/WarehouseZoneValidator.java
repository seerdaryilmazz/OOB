package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.domain.location.warehouse.WarehouseZone;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by burak on 08/02/17.
 */
public class WarehouseZoneValidator {

    public static void validate(Warehouse warehouse) {

        BigDecimal remainingZoneArea = null;

        if (warehouse.getArea() != null) {
            remainingZoneArea = new BigDecimal(warehouse.getArea().toString());
        }

        for (WarehouseZone whZone : warehouse.getZone()) {

            if (StringUtils.isBlank(whZone.getName())) {
                throw new BadRequestException("Zone Name can not be null or empty");
            }

            if (whZone.getType() == null) {
                throw new BadRequestException("Zone Type can not be null or empty");
            }

            if (whZone.getArea() != null) {
                remainingZoneArea = remainingZoneArea.subtract(whZone.getArea());
            }
            if (warehouse.getNumberOfFloors() != null && whZone.getFloorNumber() != null && warehouse.getNumberOfFloors().compareTo(whZone.getFloorNumber()) < 0) {
                throw new ValidationException("Zone '" + whZone.getName() + "' floor number '" + whZone.getFloorNumber() + "'"
                        + " can not be greater than Warehouse # of Floors (" + warehouse.getNumberOfFloors().intValue() + ")");
            }
        }

        if (remainingZoneArea != null && remainingZoneArea.signum() == -1) {
            throw new ValidationException("Total area of Zones '" + warehouse.getArea().subtract(remainingZoneArea)
                    + "'can not exceed Warehouse Area (" + warehouse.getArea().intValue());
        }

        validateRampsExist(warehouse);
    }

    private static  void validateRampsExist(Warehouse warehouse) {
        Set<Integer> rampNos = new HashSet<>();

        warehouse.getRampGroup().forEach(ramp -> {
            for (int i = ramp.getRampFrom(); i <= ramp.getRampTo(); i++) {
                rampNos.add(i);
            }
        });

        warehouse.getZone().stream().forEach(zone -> {
            if( zone.getGoodsInRamps() != null) {
                zone.getGoodsInRamps().forEach(r -> {
                    if (!rampNos.contains(r.getRampNo())) {
                        throw new ValidationException("Selected Ramp: '" + r.getRampNo() + "' does not exist.");
                    }
                });
            }
            if( zone.getGoodsOutRamps() != null) {
                zone.getGoodsOutRamps().forEach(r -> {
                    if (!rampNos.contains(r.getRampNo())) {
                        throw new ValidationException("Selected Ramp: '" + r.getRampNo() + "' does not exist.");

                    }
                });
            }
        });

    }


}
