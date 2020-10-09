package ekol.orders;

import ekol.exceptions.BadRequestException;
import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import ekol.orders.transportOrder.service.PackageTypeRestrictionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PackageTypeRestrictionServiceTest {

    private static final int MODE_CREATE = 1;
    private static final int MODE_UPDATE = 2;

    @InjectMocks
    private PackageTypeRestrictionService packageTypeRestrictionService;

    @Mock
    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    public void createUpdateRestriction(int mode) {

        PackageTypeRestriction restriction = new PackageTypeRestriction();
        PackageType packageType = new PackageType();
        BigDecimalRange grossWeightRange = new BigDecimalRange();
        BigDecimalRange netWeightRange = new BigDecimalRange();
        BigDecimalRange volumeRange = new BigDecimalRange();
        BigDecimalRange widthRange = new BigDecimalRange();
        BigDecimalRange lengthRange = new BigDecimalRange();
        BigDecimalRange heightRange = new BigDecimalRange();
        BigDecimalRange ldmRange = new BigDecimalRange();

        if (mode == MODE_CREATE) {

            // id is not null.
            restriction.setId(1L);

            try {
                packageTypeRestrictionService.createRestriction(restriction);
                fail("expected: BadRequestException msg: 'Property id must be null.'");
            } catch (BadRequestException e) {
                assertThat(e.getMessage(), equalTo("Property id must be null."));
            }

            // correct state
            restriction.setId(null);

        } else {

            // id is null.

            try {
                packageTypeRestrictionService.updateRestriction(restriction);
                fail("expected: BadRequestException msg: 'Property id cannot be null.'");
            } catch (BadRequestException e) {
                assertThat(e.getMessage(), equalTo("Property id cannot be null."));
            }

            // correct state
            restriction.setId(1L);
        }

        // packageType.id is null.
        restriction.setPackageType(packageType);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Property packageType.id cannot be null.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Property packageType.id cannot be null."));
        }

        // correct state
        packageType.setId(1L);

        if (mode == MODE_CREATE) {

            // packageType has already a restriction.
            PackageTypeRestriction anotherRestriction = new PackageTypeRestriction();

            when(packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(restriction.getPackageType().getId())).thenReturn(anotherRestriction);

            try {
                packageTypeRestrictionService.createRestriction(restriction);
                fail("expected: BadRequestException msg: 'Only one restriction can be created per package type.'");
            } catch (BadRequestException e) {
                assertThat(e.getMessage(), equalTo("Only one restriction can be created per package type."));
            }

            // correct state
            when(packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(restriction.getPackageType().getId())).thenReturn(null);
        }

        // grossWeightRange is not valid.
        grossWeightRange.setMinValue(new BigDecimal("0.13"));
        grossWeightRange.setMaxValue(new BigDecimal("0.12"));

        restriction.setGrossWeightRangeInKilograms(grossWeightRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum gross weight must be less than maximum gross weight.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum gross weight must be less than maximum gross weight."));
        }

        // correct state
        grossWeightRange.setMinValue(new BigDecimal("0.12"));
        grossWeightRange.setMaxValue(new BigDecimal("0.13"));

        // netWeightRange is not valid.
        netWeightRange.setMinValue(new BigDecimal("0.23"));
        netWeightRange.setMaxValue(new BigDecimal("0.22"));

        restriction.setNetWeightRangeInKilograms(netWeightRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum net weight must be less than maximum net weight.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum net weight must be less than maximum net weight."));
        }

        // correct state
        netWeightRange.setMinValue(new BigDecimal("0.22"));
        netWeightRange.setMaxValue(new BigDecimal("0.23"));

        // volumeRange is not valid.
        volumeRange.setMinValue(new BigDecimal("0.33"));
        volumeRange.setMaxValue(new BigDecimal("0.32"));

        restriction.setVolumeRangeInCubicMeters(volumeRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum volume must be less than maximum volume.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum volume must be less than maximum volume."));
        }

        // correct state
        volumeRange.setMinValue(new BigDecimal("0.32"));
        volumeRange.setMaxValue(new BigDecimal("0.33"));

        // widthRange is not valid.
        widthRange.setMinValue(new BigDecimal("0.43"));
        widthRange.setMaxValue(new BigDecimal("0.42"));

        restriction.setWidthRangeInCentimeters(widthRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum width must be less than maximum width.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum width must be less than maximum width."));
        }

        // correct state
        widthRange.setMinValue(new BigDecimal("0.42"));
        widthRange.setMaxValue(new BigDecimal("0.43"));

        // lengthRange is not valid.
        lengthRange.setMinValue(new BigDecimal("0.53"));
        lengthRange.setMaxValue(new BigDecimal("0.52"));

        restriction.setLengthRangeInCentimeters(lengthRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum length must be less than maximum length.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum length must be less than maximum length."));
        }

        // correct state
        lengthRange.setMinValue(new BigDecimal("0.52"));
        lengthRange.setMaxValue(new BigDecimal("0.53"));

        // heightRange is not valid.
        heightRange.setMinValue(new BigDecimal("0.63"));
        heightRange.setMaxValue(new BigDecimal("0.62"));

        restriction.setHeightRangeInCentimeters(heightRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum height must be less than maximum height.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum height must be less than maximum height."));
        }

        // correct state
        heightRange.setMinValue(new BigDecimal("0.62"));
        heightRange.setMaxValue(new BigDecimal("0.63"));

        // ldmRange is not valid.
        ldmRange.setMinValue(new BigDecimal("0.73"));
        ldmRange.setMaxValue(new BigDecimal("0.72"));

        restriction.setLdmRange(ldmRange);

        try {
            if (mode == MODE_CREATE) {
                packageTypeRestrictionService.createRestriction(restriction);
            } else {
                packageTypeRestrictionService.updateRestriction(restriction);
            }
            fail("expected: BadRequestException msg: 'Minimum ldm must be less than maximum ldm.'");
        } catch (BadRequestException e) {
            assertThat(e.getMessage(), equalTo("Minimum ldm must be less than maximum ldm."));
        }

        // correct state
        ldmRange.setMinValue(new BigDecimal("0.72"));
        ldmRange.setMaxValue(new BigDecimal("0.73"));

        // final
        when(packageTypeRestrictionRepository.save(restriction)).thenReturn(restriction);

        if (mode == MODE_CREATE) {
            packageTypeRestrictionService.createRestriction(restriction);
        } else {
            packageTypeRestrictionService.updateRestriction(restriction);
        }

        verify(packageTypeRestrictionRepository, times(1)).save(restriction);
    }

    @Test
    public void createRestriction() {
        createUpdateRestriction(MODE_CREATE);
    }

    @Test
    public void updateRestriction() {
        createUpdateRestriction(MODE_UPDATE);
    }

    @Test
    public void softDeleteRestriction() {

        Long restrictionId = 1L;

        PackageTypeRestriction restriction = new PackageTypeRestriction();
        restriction.setId(restrictionId);
        restriction.setDeleted(false);

        when(packageTypeRestrictionRepository.findOne(restrictionId)).thenReturn(restriction);

        packageTypeRestrictionService.softDeleteRestriction(restrictionId);

        verify(packageTypeRestrictionRepository, times(1)).save(restriction);

        assertTrue("restriction.isDeleted() must be true.", restriction.isDeleted());
    }

}
