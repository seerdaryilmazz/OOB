package ekol.location.util;

import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.domain.location.warehouse.WarehouseRamp;
import ekol.location.domain.location.warehouse.dto.WarehouseRampGroup;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by burak on 09/02/17.
 */
@Component
public class WarehouseRampManager {


    public Collection<WarehouseRamp> prepareRampEntries(Set<WarehouseRampGroup> rampGroups, Set<WarehouseRamp> rampsFromDB) {

        if(rampsFromDB == null) {
            rampsFromDB = new HashSet<>();
        }

        if(rampGroups == null) {
            rampGroups = new HashSet<>();
        }


        LinkedHashMap<Integer, WarehouseRamp> rampDBMap= new LinkedHashMap<>();

        rampsFromDB.forEach( ramp -> {
            rampDBMap.put(ramp.getRampNo(), ramp);
        });

        LinkedHashMap<Integer, WarehouseRamp> rampUpdatedMap = new LinkedHashMap<>();

        //create single elements from grouped elements
        rampGroups.stream().forEach(groupRamp -> {

            int startRamp = groupRamp.getRampFrom();
            int endRamp = groupRamp.getRampTo();

            for (int i = startRamp; i <= endRamp; i++) {

                WarehouseRamp ramp = rampDBMap.get(i);

                if (ramp == null) {
                    ramp = new WarehouseRamp();
                }

                ramp.setRampNo(i);
                ramp.setActive(groupRamp.isActive());
                ramp.setDeleted(groupRamp.isDeleted());
                ramp.setWarehouse(groupRamp.getWarehouse());
                ramp.setFloorNumber(groupRamp.getFloorNumber());
                ramp.setProperty(groupRamp.getProperty());

                rampUpdatedMap.put(ramp.getRampNo(), ramp);

            }
        });

        rampDBMap.entrySet().forEach( e -> {
            if(!rampUpdatedMap.containsKey(e.getKey())) {
                e.getValue().setDeleted(true);
                rampUpdatedMap.put(e.getKey(), e.getValue());
            }
        });



        return rampUpdatedMap.values();

    }



    public Warehouse groupRamps(Warehouse warehouse) {

        HashMap<Integer, WarehouseRamp> rampMap = new HashMap<>();


        if(warehouse.getRamp() != null) {
            warehouse.getRamp().forEach(ramp -> {
                rampMap.put(ramp.getRampNo(), ramp);
            });
        }


        LinkedHashSet<WarehouseRampGroup> rampGroup = new LinkedHashSet<>();
        WarehouseRampGroup rampGroupElem = null;

        int i = 0;
        while (true) {
            if(rampMap.isEmpty()) {
                break;
            }

            WarehouseRamp ramp = rampMap.get(i);

            if (ramp == null || ramp.isDeleted()) {
                rampGroupElem = null;
            } else if (rampGroupElem == null) {
                rampGroupElem = new WarehouseRampGroup(ramp);
                rampGroup.add(rampGroupElem);
            } else {
                if(isContentEqual(rampGroupElem, ramp)) {
                    rampGroupElem.setRampTo(i);
                } else {
                    rampGroupElem = new WarehouseRampGroup(ramp);
                    rampGroup.add(rampGroupElem);
                }
            }

            rampMap.remove(i);

            if( i == Integer.MAX_VALUE) {
                break;
            }

            i++;
        }


        warehouse.setRampGroup(rampGroup);

        return warehouse;
    }

    private boolean isContentEqual(WarehouseRampGroup warehouseRampGroup, WarehouseRamp warehouseRamp) {

        if (warehouseRampGroup.isActive() == warehouseRamp.isActive()
                && warehouseRampGroup.isDeleted() == warehouseRamp.isDeleted()
                && warehouseRampGroup.getProperty() == warehouseRamp.getProperty()
                && warehouseRampGroup.getFloorNumber() == warehouseRamp.getFloorNumber()
                && warehouseRampGroup.getWarehouse().getId().compareTo(warehouseRamp.getWarehouse().getId()) == 0) {
            return true;
        }

        return false;
    }

}
