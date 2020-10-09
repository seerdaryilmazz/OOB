package ekol.orders.transportOrder.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import ekol.orders.transportOrder.service.PackageTypeRestrictionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/package-type/restriction")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PackageTypeRestrictionApiController {

    private static final String CANNOT_BE_LESS_THAN = "{0} cannot be less than {1}.";
    private static final String CANNOT_BE_GREATER_THAN = "{0} cannot be greater than {1}.";

    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;
    private PackageTypeRestrictionService packageTypeRestrictionService;

    @GetMapping(value = {"", ""})
    public PackageTypeRestriction findByPackageTypeId(@RequestParam(value = "packageTypeId") Long packageTypeId) {

        return packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(packageTypeId);
    }

    @Authorize(operations="order.package-type.manage")
    @PostMapping(value = {"", "/"})
    public PackageTypeRestriction create(@RequestBody PackageTypeRestriction restriction) {

        return packageTypeRestrictionService.createRestriction(restriction);
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    public PackageTypeRestriction findById(@PathVariable Long id) {

        return packageTypeRestrictionRepository.findOne(id);
    }

    @Authorize(operations="order.package-type.manage")
    @PutMapping(value = {"/{id}", "/{id}/"})
    public PackageTypeRestriction update(@PathVariable Long id, @RequestBody PackageTypeRestriction restriction) {

        if (!id.equals(restriction.getId())) {
            throw new BadRequestException("Restriction.id must be " + id + ".");
        }

        return packageTypeRestrictionService.updateRestriction(restriction);
    }

    @Authorize(operations="order.package-type.manage")
    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {

        packageTypeRestrictionService.softDeleteRestriction(id);
    }

    @GetMapping(
            value = {
                    "/{checkType}/{packageTypeId}/{value:.+}",
                    "/{checkType}/{packageTypeId}/{value:.+}/"
            })
    public void checkBigDecimalProperty(@PathVariable String checkType,
                                        @PathVariable Long packageTypeId,
                                        @PathVariable BigDecimal value) {

        PackageTypeRestriction restriction = packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(packageTypeId);
        
        if (restriction != null) {
            if ("checkGrossWeight".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getGrossWeightRangeInKilograms(), value, "Gross weight");
            } else if ("checkNetWeight".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getNetWeightRangeInKilograms(), value, "Net weight");
            } else if ("checkVolume".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getVolumeRangeInCubicMeters(), value, "Volume");
            } else if ("checkWidth".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getWidthRangeInCentimeters(), value, "Width");
            } else if ("checkLength".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getLengthRangeInCentimeters(), value, "Length");
            } else if ("checkHeight".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getHeightRangeInCentimeters(), value, "Height");
            } else if ("checkLdm".equals(checkType)) {
                generateBadRequestIfValueIsNotInRange(restriction.getLdmRange(), value, "Ldm");
            } else {
                throw new ResourceNotFoundException("Invalid checkType: " + checkType);
            }
        }
    }

    private static void generateBadRequestIfValueIsNotInRange(BigDecimalRange range, BigDecimal value, String propertyDesc) {

        if (range != null) {

            int result = range.check(value);

            if (result == BigDecimalRange.VALUE_IS_LESS_THAN_MIN) {
                throw new BadRequestException(CANNOT_BE_LESS_THAN, propertyDesc, range.getMinValue());
            } else if (result == BigDecimalRange.VALUE_IS_GREATER_THAN_MAX) {
                throw new BadRequestException(CANNOT_BE_GREATER_THAN, propertyDesc, range.getMaxValue());
            }
        }
    }

}
