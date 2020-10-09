package ekol.orders.transportOrder.service;

import ekol.exceptions.BadRequestException;
import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageTypeRestrictionService {

    private static final int MODE_CREATE = 1;
    private static final int MODE_UPDATE = 2;

    @Autowired
    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    private PackageTypeRestriction createUpdateRestriction(int mode, PackageTypeRestriction restriction) {

        checkRestrictionId(mode, restriction);
        checkRestrictionParent(mode, restriction);
        checkGrossWeightRange(restriction);
        checkNetWeightRange(restriction);
        checkVolumeRange(restriction);
        checkWidthRange(restriction);
        checkLengthRange(restriction);
        checkHeightRange(restriction);
        checkLdmRange(restriction);

        return packageTypeRestrictionRepository.save(restriction);
    }

    private static void checkRestrictionId(int mode, PackageTypeRestriction restriction) {

        if (mode == MODE_CREATE && restriction.getId() != null) {
            throw new BadRequestException("Property id must be null.");
        }

        if (mode == MODE_UPDATE && restriction.getId() == null) {
            throw new BadRequestException("Property id cannot be null.");
        }
    }

    private void checkRestrictionParent(int mode, PackageTypeRestriction restriction) {

        if (restriction.getPackageType() == null || restriction.getPackageType().getId() == null) {
            throw new BadRequestException("Property packageType.id cannot be null.");
        }

        if (mode == MODE_CREATE &&
                packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(restriction.getPackageType().getId()) != null) {
            throw new BadRequestException("Only one restriction can be created per package type.");
        }
    }

    private static boolean isRangeValid(BigDecimalRange range) {

        boolean result = true;

        if (range != null && range.getMinValue() != null && range.getMaxValue() != null &&
                range.getMinValue().compareTo(range.getMaxValue()) > 0) {
            result = false;
        }

        return result;
    }

    private static void checkGrossWeightRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getGrossWeightRangeInKilograms())) {
            throw new BadRequestException("Minimum gross weight must be less than maximum gross weight.");
        }
    }

    private static void checkNetWeightRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getNetWeightRangeInKilograms())) {
            throw new BadRequestException("Minimum net weight must be less than maximum net weight.");
        }
    }

    private static void checkVolumeRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getVolumeRangeInCubicMeters())) {
            throw new BadRequestException("Minimum volume must be less than maximum volume.");
        }
    }

    private static void checkWidthRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getWidthRangeInCentimeters())) {
            throw new BadRequestException("Minimum width must be less than maximum width.");
        }
    }

    private static void checkLengthRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getLengthRangeInCentimeters())) {
            throw new BadRequestException("Minimum length must be less than maximum length.");
        }
    }

    private static void checkHeightRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getHeightRangeInCentimeters())) {
            throw new BadRequestException("Minimum height must be less than maximum height.");
        }
    }

    private static void checkLdmRange(PackageTypeRestriction restriction) {
        if (!isRangeValid(restriction.getLdmRange())) {
            throw new BadRequestException("Minimum ldm must be less than maximum ldm.");
        }
    }

    public PackageTypeRestriction createRestriction(PackageTypeRestriction restriction) {
        return createUpdateRestriction(MODE_CREATE, restriction);
    }

    public PackageTypeRestriction updateRestriction(PackageTypeRestriction restriction) {
        return createUpdateRestriction(MODE_UPDATE, restriction);
    }

    public void softDeleteRestriction(Long id) {

        PackageTypeRestriction restriction = packageTypeRestrictionRepository.findOne(id);

        if (restriction != null) {
            restriction.setDeleted(true);
            packageTypeRestrictionRepository.save(restriction);
        }
    }

}
