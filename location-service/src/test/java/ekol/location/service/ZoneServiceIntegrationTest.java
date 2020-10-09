package ekol.location.service;

import ekol.hibernate5.test.TestUtils;
import ekol.location.domain.*;
import ekol.location.repository.*;
import ekol.location.testdata.SomeData;
import org.apache.commons.collections.IteratorUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ozer on 02/02/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class ZoneServiceIntegrationTest {

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ZoneTagRepository zoneTagRepository;

    @Autowired
    private ZoneZipCodeRepository zoneZipCodeRepository;

    @Autowired
    private ZonePolygonRegionRepository zonePolygonRegionRepository;

    public void cleanup() {
        TestUtils.softDeleteAll(
                zoneTypeRepository,
                countryRepository,
                polygonRegionRepository,
                zoneRepository,
                zoneTagRepository,
                zoneZipCodeRepository,
                zonePolygonRegionRepository
        );
    }

    @Before
    public void runBeforeEveryTest() {
        cleanup();
    }

    @After
    public void runAfterEveryTest() {
        cleanup();
    }

    @Test
    public void saveMustWorkCorrectly() {
        ZoneType zoneType = zoneTypeRepository.save(SomeData.someZoneType());
        Country country = countryRepository.save(SomeData.someCountry());
        PolygonRegion polygonRegion = polygonRegionRepository.save(SomeData.somePolygonRegion());

        Set<ZoneTag> tags = new HashSet<>(Arrays.asList(SomeData.someZoneTag(null)));
        Set<ZoneZipCode> zipCodes = new HashSet<>(Arrays.asList(SomeData.someZoneZipCode(null, country)));
        Set<ZonePolygonRegion> polygonRegions = new HashSet<>(Arrays.asList(SomeData.someZonePolygonRegion(null, polygonRegion, null)));

        Zone zone = SomeData.someZone(zoneType, polygonRegions, tags, zipCodes);
        zoneService.save(zone);

        List<Zone> zoneList = IteratorUtils.toList(zoneRepository.findAll().iterator());
        List<ZoneTag> zoneTagList = IteratorUtils.toList(zoneTagRepository.findAll().iterator());
        List<ZoneZipCode> zoneZipCodeList = IteratorUtils.toList(zoneZipCodeRepository.findAll().iterator());
        List<ZonePolygonRegion> zonePolygonRegionList = IteratorUtils.toList(zonePolygonRegionRepository.findAll().iterator());

        Assert.assertEquals("should have 1 zone", 1, zoneList.size());
        Assert.assertEquals("should have 1 zone tag", 1, zoneTagList.size());
        Assert.assertEquals("should have 1 zone zip code", 1, zoneZipCodeList.size());
        Assert.assertEquals("should have 1 zone polygon region", 1, zonePolygonRegionList.size());
    }
}
