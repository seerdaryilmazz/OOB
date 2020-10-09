package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.domain.location.warehouse.WarehouseRamp;
import ekol.location.domain.location.warehouse.WarehouseZone;
import ekol.location.repository.WarehouseZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by burak on 09/02/17.
 */
@Service
public class WarehouseZoneService {

    @Autowired
    private WarehouseZoneRepository warehouseZoneRepository;

    public void handleWarehouseZones(Warehouse warehouse, Set<WarehouseZone> zones, Collection<WarehouseRamp> ramps) {

        if (warehouse == null) {
            throw new BadRequestException("Warehouse is null");
        }


        Set<WarehouseZone> zonesFromDb = warehouse.getZone();

        List<Long> zoneIds = zones.stream().filter(z -> z.getId() != null).map(z -> {
            return z.getId().longValue();
        }).collect(Collectors.toList());


        zonesFromDb.forEach(zdb -> {
            if (!zoneIds.contains(zdb.getId())) {
                zdb.setDeleted(true);
                warehouseZoneRepository.save(zdb);
            }
        });


        zones.forEach(zone -> {

            initializeRamps(zone, ramps);

            if (zone.getId() == null) {
                warehouseZoneRepository.save(zone);
            } else {
                WarehouseZone zoneFromDb = zonesFromDb.stream().filter(zdb -> {
                    return zone.getId().compareTo(zdb.getId()) == 0;
                }).findAny().orElse(null);

                if (zoneFromDb == null) {
                    throw new BadRequestException("Warehouse Zone to be edit does not exist");
                }
                warehouseZoneRepository.save(zone);
            }

        });
    }

    private void initializeRamps(WarehouseZone zone, Collection<WarehouseRamp> ramps) {

        if (zone.getGoodsInRamps() != null) {
            for (int i = 0; i < zone.getGoodsInRamps().size(); i++) {
                if (zone.getGoodsInRamps().get(i).getId() == null) {
                    int rampInNo = zone.getGoodsInRamps().get(i).getRampNo();
                    WarehouseRamp rampIn = ramps.stream().filter(f -> f.getRampNo() == rampInNo).findAny().orElse(null);

                    if (rampIn == null) {
                        throw new ValidationException("Ramp with ramp no: " + rampInNo + " does not exist.");
                    }
                    zone.getGoodsInRamps().set(i, rampIn);
                }
            }
        }


        if (zone.getGoodsOutRamps() != null) {
            for (int i = 0; i < zone.getGoodsOutRamps().size(); i++) {
                if (zone.getGoodsOutRamps().get(i).getId() == null) {
                    int rampOutNo = zone.getGoodsOutRamps().get(i).getRampNo();
                    WarehouseRamp rampOut = ramps.stream().filter(f -> f.getRampNo() == rampOutNo).findAny().orElse(null);

                    if (rampOut == null) {
                        throw new ValidationException("Ramp with ramp no: " + rampOutNo + " does not exist.");
                    }
                    zone.getGoodsOutRamps().set(i, rampOut);
                }
            }
        }

    }


 /*   public void handleWarehouseZones(Long warehouseId, Set<WarehouseZone> zones, Collection<WarehouseRamp> ramps) {

        if (warehouseId == null) {
            throw new BadRequestException("Warehouse Id is null");
        }

        Warehouse warehouseFromDb = warehouseRepository.findOne(warehouseId);
        if (warehouseFromDb == null) {
            throw new ResourceNotFoundException("Warehouse is not found");
        }


        List<WarehouseZone> zonesFromDb = warehouseZoneRepository.findAllByWarehouseId(warehouseId);

        List<Long> zoneIds = zones.stream().filter(z -> z.getId() != null).map(z -> {
            return z.getId().longValue();
        }).collect(Collectors.toList());


        zonesFromDb.forEach(zdb -> {
            if (!zoneIds.contains(zdb.getId())) {
                delete(zdb);
            }
        });


        zones.forEach(zone -> {

            initializeRamps(zone, ramps);

            if (zone.getId() == null) {
                add(warehouseFromDb, zone);
            } else {
                WarehouseZone zoneFromDb = zonesFromDb.stream().filter(zdb -> {
                    return zone.getId().compareTo(zdb.getId()) == 0;
                }).findAny().orElse(null);

                if (zoneFromDb == null) {
                    throw new BadRequestException("Warehouse Zone to be edit does not exist");
                }
                edit(zoneFromDb, zone);
            }

        });
    }

    private void initializeRamps(WarehouseZone zone, Collection<WarehouseRamp> ramps) {

        if (zone.getGoodsInRamps() != null) {
            for (int i = 0; i < zone.getGoodsInRamps().size(); i++) {
                if (zone.getGoodsInRamps().get(i).getId() != null) {
                    int rampInNo = zone.getGoodsInRamps().get(i).getRampNo();
                    WarehouseRamp rampIn = ramps.stream().filter(f -> f.getRampNo() == rampInNo).findAny().orElse(null);

                    if (rampIn == null) {
                        throw new ValidationException("Ramp with ramp no: " + rampInNo + " does not exist.");
                    }
                    zone.getGoodsInRamps().set(i, rampIn);
                }
            }
        }


        if (zone.getGoodsOutRamps() != null) {
            for (int i = 0; i < zone.getGoodsOutRamps().size(); i++) {
                if (zone.getGoodsOutRamps().get(i).getId() == null) {
                    int rampOutNo = zone.getGoodsOutRamps().get(i).getRampNo();
                    WarehouseRamp rampOut = ramps.stream().filter(f -> f.getRampNo() == rampOutNo).findAny().orElse(null);

                    if (rampOut == null) {
                        throw new ValidationException("Ramp with ramp no: " + rampOutNo + " does not exist.");
                    }
                    zone.getGoodsOutRamps().set(i, rampOut);
                }
            }
        }

    }

    private WarehouseZone add(Warehouse warehouse, WarehouseZone warehouseZone) {

        warehouseZone.setWarehouse(warehouse);

        return warehouseZoneRepository.save(warehouseZone);

    }

    private WarehouseZone edit(WarehouseZone warehouseZoneFromDb, WarehouseZone warehouseZone) {

        warehouseZoneFromDb.setName(warehouseZone.getName());
        warehouseZoneFromDb.setType(warehouseZone.getType());
        warehouseZoneFromDb.setArea(warehouseZone.getArea());
        warehouseZoneFromDb.setHeight(warehouseZone.getHeight());
        warehouseZoneFromDb.setFloorNumber(warehouseZone.getFloorNumber());
        warehouseZoneFromDb.setGoodsInRamps(warehouseZone.getGoodsInRamps());
        warehouseZoneFromDb.setGoodsOutRamps(warehouseZone.getGoodsOutRamps());
        warehouseZoneFromDb.setRampSelectionForGoodsIn(warehouseZone.getRampSelectionForGoodsIn());
        warehouseZoneFromDb.setRampSelectionForGoodsOut(warehouseZone.getRampSelectionForGoodsOut());

        return warehouseZoneRepository.save(warehouseZoneFromDb);

    }

    private WarehouseZone delete(WarehouseZone zoneFromDb) {
        zoneFromDb.setDeleted(true);

        return warehouseZoneRepository.save(zoneFromDb);
    }*/

}
