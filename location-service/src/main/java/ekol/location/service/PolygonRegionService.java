package ekol.location.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import com.vividsolutions.jts.geom.MultiPolygon;
import ekol.exceptions.ApplicationException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.*;
import ekol.location.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PolygonRegionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonRegionService.class);

    private static final Comparator<Coordinate> COORDINATE_SORTING_COMPARATOR = new Comparator<Coordinate>() {
        @Override
        public int compare(Coordinate c1, Coordinate c2) {
            return c1.getOrderNo().compareTo(c2.getOrderNo());
        }
    };

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private EncodedCoordinatesStringRepository encodedCoordinatesStringRepository;

    @Autowired
    private CoordinateRingRepository coordinateRingRepository;

    @Autowired
    private PolygonRepository polygonRepository;

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    @Autowired
    private AdministrativeRegionPostcodeRepository administrativeRegionPostcodeRepository;

    @Autowired
    private PolygonRegionToAdministrativeRegionRepository polygonRegionToAdministrativeRegionRepository;

    @Autowired
    private PostcodeRepository postcodeRepository;

    public PolygonRegion findByIdOrThrowResourceNotFoundException(Long id) {

        PolygonRegion persistedEntity = polygonRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Polygon region with specified id cannot be found: " + id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteExistingPolygonRegions(String countryIsoAlpha3Code, Integer level) {
        List<PolygonRegion> existingRegions = polygonRegionRepository.findAllByCountryIsoAlpha3CodeAndLevel(countryIsoAlpha3Code, level);
        for (PolygonRegion r : existingRegions) {
            softDeletePolygonRegion(r);
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void processShapeFile(String directoryPath, String countryIsoAlpha3Code, Integer level, boolean saveCoordinatesIndividually) {

        DataStore dataStore = null;
        FeatureIterator iterator = null;

        try {

            if (level == 0 || level == 1 || level == 2) {

                File shapeFile = Paths.get(directoryPath).resolve(countryIsoAlpha3Code + "_adm" + level + ".shp").toFile();

                Map connectionParams = new HashMap();
                connectionParams.put("url", shapeFile.toURI().toURL());
                connectionParams.put("charset", "UTF-8");

                dataStore = DataStoreFinder.getDataStore(connectionParams);
                String[] typeNames = dataStore.getTypeNames();
                String typeName = typeNames[0];
                FeatureSource featureSource = dataStore.getFeatureSource(typeName);
                FeatureCollection collection = featureSource.getFeatures();
                iterator = collection.features();

                while (iterator.hasNext()) {

                    Feature feature = iterator.next();
                    PolygonRegion region = null;

                    if (level == 0) {

                        String NAME_ENGLISH = feature.getProperty("NAME_ENGLI").getValue().toString();
                        String NAME_LOCAL = feature.getProperty("NAME_LOCAL").getValue().toString();

                        region = createPolygonRegionLevel0(countryIsoAlpha3Code, NAME_ENGLISH, NAME_LOCAL);

                    } else if (level == 1) {

                        String NAME_0 = feature.getProperty("NAME_0").getValue().toString();
                        String NAME_1 = feature.getProperty("NAME_1").getValue().toString();
                        String VARNAME_1 = feature.getProperty("VARNAME_1").getValue().toString();

                        region = createPolygonRegionLevel1(countryIsoAlpha3Code, NAME_0, NAME_1, VARNAME_1);

                    } else if (level == 2) {

                        String NAME_0 = feature.getProperty("NAME_0").getValue().toString();
                        String NAME_1 = feature.getProperty("NAME_1").getValue().toString();
                        String NAME_2 = feature.getProperty("NAME_2").getValue().toString();
                        String VARNAME_2 = feature.getProperty("VARNAME_2").getValue().toString();
                        String TYPE_2 = feature.getProperty("TYPE_2").getValue().toString();

                        region = createPolygonRegionLevel2(countryIsoAlpha3Code, NAME_0, NAME_1, NAME_2, VARNAME_2, TYPE_2);
                    }

                    MultiPolygon multiPolygon = (MultiPolygon) feature.getProperty("the_geom").getValue();

                    for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                        region.getPolygons().add(getPolygon((com.vividsolutions.jts.geom.Polygon) multiPolygon.getGeometryN(i), saveCoordinatesIndividually,
                                countryIsoAlpha3Code, level));
                    }

                    savePolygonRegionInNewTransaction(region);
                }

            } else {
                throw new ApplicationException("Levels greater than 2 are not supported currently.");
            }

        } catch (Exception e) {
            LOGGER.debug("Exception while processing shp file: " + directoryPath + "," + level, e);
            throw new ApplicationException("Exception while processing shp file: " + directoryPath + ", " + level, e);
        } finally {
            if (iterator != null) {
                iterator.close();
            }
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePolygonRegionInNewTransaction(PolygonRegion region) {
        PolygonRegion persistedRegion = polygonRegionRepository.save(region);
        savePolygons(persistedRegion, region.getPolygons());
    }

    @Transactional
    public void savePolygonRegion(PolygonRegion region) {
        PolygonRegion persistedRegion = polygonRegionRepository.save(region);
        savePolygons(persistedRegion, region.getPolygons());
    }

    @Transactional
    public void savePolygons(PolygonRegion persistedRegion, Set<Polygon> polygons) {

        Polygon persistedPolygon = null;

        for (Polygon polygon : polygons) {
            polygon.setRegion(persistedRegion);
            persistedPolygon = polygonRepository.save(polygon);
            saveCoordinateRings(persistedPolygon, polygon.getRings());
        }
    }

    @Transactional
    public void saveCoordinateRings(Polygon persistedPolygon, Set<CoordinateRing> rings) {

        EncodedCoordinatesString persistedEncodedCoordinatesString = null;
        CoordinateRing persistedRing = null;

        for (CoordinateRing ring : rings) {
            persistedEncodedCoordinatesString = saveEncodedCoordinatesString(ring.getEncodedCoordinatesString());
            ring.setPolygon(persistedPolygon);
            ring.setEncodedCoordinatesString(persistedEncodedCoordinatesString);
            persistedRing = coordinateRingRepository.save(ring);
            saveCoordinates(persistedRing, ring.getCoordinates());
        }
    }

    @Transactional
    public EncodedCoordinatesString saveEncodedCoordinatesString(EncodedCoordinatesString encodedCoordinatesString) {
        return encodedCoordinatesStringRepository.save(encodedCoordinatesString);
    }

    @Transactional
    public void saveCoordinates(CoordinateRing persistedRing, Set<Coordinate> coordinates) {

        for (Coordinate coordinate : coordinates) {
            coordinate.setRing(persistedRing);
            coordinateRepository.save(coordinate);
        }
    }

    @Transactional
    public void softDeletePolygonRegion(PolygonRegion region) {
        for (Polygon p : region.getPolygons()) {
            softDeletePolygon(p);
        }
        region.setDeleted(true);
        polygonRegionRepository.save(region);
    }

    @Transactional
    public void softDeletePolygon(Polygon polygon) {
        for (CoordinateRing r : polygon.getRings()) {
            softDeleteCoordinateRing(r);
        }
        polygon.setDeleted(true);
        polygonRepository.save(polygon);
    }

    @Transactional
    public void softDeleteCoordinateRing(CoordinateRing ring) {
        for (Coordinate c : ring.getCoordinates()) {
            softDeleteCoordinate(c);
        }
        ring.setDeleted(true);
        coordinateRingRepository.save(ring);
        softDeleteEncodedCoordinatesString(ring.getEncodedCoordinatesString());
    }

    @Transactional
    public void softDeleteEncodedCoordinatesString(EncodedCoordinatesString encodedCoordinatesString) {
        encodedCoordinatesString.setDeleted(true);
        encodedCoordinatesStringRepository.save(encodedCoordinatesString);
    }

    @Transactional
    public void softDeleteCoordinate(Coordinate coordinate) {
        coordinate.setDeleted(true);
        coordinateRepository.save(coordinate);
    }

    public PolygonRegion findLevel0PolygonRegionByCountryIsoAlpha3Code(String countryIsoAlpha3Code) {

        PolygonRegion region;
        List<PolygonRegion> regionList = polygonRegionRepository.findAllForDrawingDistinctByCountryIsoAlpha3CodeAndLevel(countryIsoAlpha3Code, 0);

        if (regionList.size() == 0) {
            throw new ResourceNotFoundException("Polygon region with specified country iso and level cannot be found: " + countryIsoAlpha3Code + ", " + 0);
        } else if (regionList.size() == 1) {
            region = regionList.get(0);
        } else {
            throw new ApplicationException("There must be only one polygon region with specified country iso and level: " + countryIsoAlpha3Code + ", " + 0);
        }

        return region;
    }

    public List<PolygonRegion> findPolygonRegionsByParent(String parent) {

        List<PolygonRegion> regionList = polygonRegionRepository.findAllForDrawingDistinctByParent(parent);

        if (regionList.size() == 0) {
            throw new ResourceNotFoundException("No polygon regions found with specified parent");
        }

        return regionList;
    }

    public List<Long> findIdsOfPolygonRegionsByParent(String parent) {

        List<Long> ids = new ArrayList<>();
        List<PolygonRegion> regionList = polygonRegionRepository.findAllByParent(parent);

        if (regionList.size() == 0) {
            throw new ResourceNotFoundException("No polygon regions found with specified parent");
        } else {
            for (PolygonRegion region : regionList) {
                ids.add(region.getId());
            }
        }

        return ids;
    }

    public PolygonRegion findForDrawingByIdOrThrowResourceNotFoundException(Long id) {

        PolygonRegion persistedEntity = polygonRegionRepository.findForDrawingById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Polygon region with specified id cannot be found: " + id);
        }
    }

    public List<PolygonRegion> findPolygonRegionsByIds(List<Long> ids) {

        List<PolygonRegion> regionList = polygonRegionRepository.findAllForDrawingDistinctByIdIn(ids);

        if (regionList.size() == 0) {
            throw new ResourceNotFoundException("No polygon regions found with specified ids");
        }

        return regionList;
    }

    public PolygonRegion findPolygonRegionByParentAndName(String parent, String name) {

        PolygonRegion region = polygonRegionRepository.findForDrawingDistinctByParentAndName(parent, name);

        if (region == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        return region;
    }

    public Long findIdOfPolygonRegionByParentAndName(String parent, String name) {

        PolygonRegion region = polygonRegionRepository.findByParentAndName(parent, name);

        if (region == null) {
            throw new ResourceNotFoundException("Polygon region with specified parent and name cannot be found: " + parent + ", " + name);
        }

        return region.getId();
    }

    public List<PolygonRegion> findPolygonRegionsByPostcodeStartsWith(Country country, String postcodeStartsWith, boolean searchForDrawing) {
        return findPolygonRegionsByPostcode(country, postcodeStartsWith + "%", searchForDrawing);
    }

    public List<PolygonRegion> findPolygonRegionsByPostcode(Country country, String postcodeValue, boolean searchForDrawing) {

        List<Postcode> postcodes = postcodeRepository.findAllWithDetailsDistinctByCountryIsoAlpha3CodeAndValueLike(country.getIsoAlpha3Code(), postcodeValue);

        Map<Long, PolygonRegion> polygonRegionMap = new HashMap<>();

        for (Postcode p : postcodes) {
            // Bazı posta kodları hiçbir polygonRegion ile eşleşmemiş olabiliyor.
            if (p.getPolygonRegion() != null) {
                polygonRegionMap.put(p.getPolygonRegion().getId(), p.getPolygonRegion());
            }
        }

        if (searchForDrawing) {
            return polygonRegionRepository.findAllForDrawingDistinctByIdIn(new ArrayList<>(polygonRegionMap.keySet()));
        } else {
            return new ArrayList<>(polygonRegionMap.values());
        }
    }

    public List<PolygonRegion> findPolygonRegionsByPostcodeStartsWith(String countryIsoAlpha2Code, String postcodeStartsWith) {

        String queryParam = postcodeStartsWith + "%";

        // TODO: İlk önce kayıtları say, fazla kayıt varsa uyarı ver.

        List<AdministrativeRegionPostcode> admRegionPostcodeList =
                administrativeRegionPostcodeRepository.findAllByRegion_CountryIsoAlpha2CodeAndPostcodeLike(countryIsoAlpha2Code, queryParam);

        return findPolygonRegionsByAdmRegionPostcodeList(admRegionPostcodeList);
    }

    public List<PolygonRegion> findPolygonRegionsByPostcode(String countryIsoAlpha2Code, String postcode) {

        // TODO: Bir posta kodu aynı ülke içinde sadece bir bölge için tanımlanmış olabilir. Dolayısıyla liste yerine bir kayıt dönmeli.

        List<AdministrativeRegionPostcode> admRegionPostcodeList =
                administrativeRegionPostcodeRepository.findAllByRegion_CountryIsoAlpha2CodeAndPostcode(countryIsoAlpha2Code, postcode);

        return findPolygonRegionsByAdmRegionPostcodeList(admRegionPostcodeList);
    }

    // TODO: Bu metodtaki DB sorgularını mümkün olduğu kadar azaltmak lazım. İyileştirme için bu metodu kullanan metodları da incelemek gerekebilir.
    private List<PolygonRegion> findPolygonRegionsByAdmRegionPostcodeList(List<AdministrativeRegionPostcode> admRegionPostcodeList) {

        List<PolygonRegion> polygonRegionList = new ArrayList<>();

        Map<Long, AdministrativeRegion> admRegionMap = new HashMap<>();

        for (AdministrativeRegionPostcode admRegionPostcode : admRegionPostcodeList) {
            admRegionMap.put(admRegionPostcode.getRegion().getId(), admRegionPostcode.getRegion());
        }

        List<String> admRegionHierarchicalNameList = new ArrayList<>();

        for (AdministrativeRegion admRegion : admRegionMap.values()) {
            admRegionHierarchicalNameList.add(getHierarchicalName(admRegion));
        }

        List<PolygonRegionToAdministrativeRegion> polygonRegionToAdmRegionList =
                polygonRegionToAdministrativeRegionRepository.findAllByAdministrativeRegionIn(admRegionHierarchicalNameList);

        for (PolygonRegionToAdministrativeRegion polygonRegionToAdmRegion : polygonRegionToAdmRegionList) {
            String polygonRegionHierarchicalName = polygonRegionToAdmRegion.getPolygonRegion();
            String parent = polygonRegionHierarchicalName.substring(0, polygonRegionHierarchicalName.lastIndexOf("/"));
            if (parent.length() == 0) {
                parent = "/";
            }
            String name = polygonRegionHierarchicalName.substring(polygonRegionHierarchicalName.lastIndexOf("/") + 1);
            PolygonRegion polygonRegion = polygonRegionRepository.findForDrawingDistinctByParentAndName(parent, name);
            // TODO: Kayıt null ise mapping tablosu (yani PolygonRegionToAdministrativeRegion) güncel olmayabilir, bu durumda ne yapalım?
            if (polygonRegion != null) {
                polygonRegionList.add(polygonRegion);
            }
        }

        return polygonRegionList;
    }

    // TODO: İyileştir: http://stackoverflow.com/a/5931267
    private String getHierarchicalName(AdministrativeRegion administrativeRegion) {
        StringBuilder stringBuilder = new StringBuilder();
        AdministrativeRegion administrativeRegionTemp = administrativeRegion;
        while (administrativeRegionTemp != null) {
            stringBuilder.insert(0, "/" + administrativeRegionTemp.getName());
            administrativeRegionTemp = administrativeRegionTemp.getParent();
        }
        return stringBuilder.toString();
    }

    private PolygonRegion createPolygonRegionLevel0(String countryIsoAlpha3Code, String nameEnglish, String nameLocal) {

        PolygonRegion r = new PolygonRegion();
        r.setParent("/");
        r.setCountryIsoAlpha3Code(countryIsoAlpha3Code);
        r.setName(normalizeString(nameEnglish));
        r.setLocalName(normalizeString(nameLocal));
        r.setLevel(0);

        return r;
    }

    private PolygonRegion createPolygonRegionLevel1(String countryIsoAlpha3Code, String name0, String name1, String varname1) {

        if (varname1 == null || varname1.trim().length() == 0) {
            varname1 = name1;
        }

        PolygonRegion r = new PolygonRegion();
        r.setParent("/" + normalizeString(name0));
        r.setCountryIsoAlpha3Code(countryIsoAlpha3Code);
        r.setName(normalizeString(name1));
        r.setLocalName(normalizeString(varname1));
        r.setLevel(1);

        return r;
    }

    private PolygonRegion createPolygonRegionLevel2(String countryIsoAlpha3Code, String name0, String name1, String name2,
                                                           String varname2, String type2) {

        if (varname2 == null || varname2.trim().length() == 0) {
            varname2 = name2;
        } else {
            varname2 = varname2.replaceAll(type2, "").trim();
        }

        PolygonRegion r = new PolygonRegion();
        r.setParent("/" + normalizeString(name0) + "/" + normalizeString(name1));
        r.setCountryIsoAlpha3Code(countryIsoAlpha3Code);
        r.setName(normalizeString(name2));
        r.setLocalName(normalizeString(varname2));
        r.setLevel(2);

        return r;
    }

    private String generateEncodedCoordinatesString(Set<Coordinate> coordinates) {

        List<Coordinate> coordinateListOrdered = new ArrayList<>(coordinates);

        coordinateListOrdered.sort(COORDINATE_SORTING_COMPARATOR);

        List<LatLng> latLngList = new ArrayList<>();

        for (Coordinate coordinate : coordinateListOrdered) {
            latLngList.add(new LatLng(coordinate.getLatitude().doubleValue(), coordinate.getLongitude().doubleValue()));
        }

        return PolylineEncoding.encode(latLngList);
    }

    private void setMinMaxCoordinates(CoordinateRing ring, Set<Coordinate> coordinates) {

        BigDecimal minLongitude = null;
        BigDecimal maxLongitude = null;
        BigDecimal minLatitude = null;
        BigDecimal maxLatitude = null;

        for (Coordinate c : coordinates) {

            minLongitude = (minLongitude == null ? c.getLongitude() : minLongitude.min(c.getLongitude()));
            maxLongitude = (maxLongitude == null ? c.getLongitude() : maxLongitude.max(c.getLongitude()));

            minLatitude = (minLatitude == null ? c.getLatitude() : minLatitude.min(c.getLatitude()));
            maxLatitude = (maxLatitude == null ? c.getLatitude() : maxLatitude.max(c.getLatitude()));
        }

        ring.setMinLongitude(minLongitude);
        ring.setMaxLongitude(maxLongitude);
        ring.setMinLatitude(minLatitude);
        ring.setMaxLatitude(maxLatitude);
    }

    private String normalizeString(String string) {

        // '/' karakterini alt ve üst bölgeler arasında ayıraç olarak kullanıyoruz.
        string = string.replaceAll("/", "-");

        // Aşağısı 'Ł' için doğru çalışmıyor.
        // String result = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        // commons-lang3 versiyonu 3.5 üstü olmalı yoksa doğru çalışmıyor.
        String result = StringUtils.stripAccents(string);
        if (string.length() != result.length()) {
            throw new RuntimeException("String normalization failed. input: " + string + ", output: " + result);
        }
        return result;
    }

    private Polygon getPolygon(com.vividsolutions.jts.geom.Polygon polygon, boolean saveCoordinatesIndividually,
                               String countryIsoAlpha3Code, Integer level) {

        Polygon p = new Polygon();
        p.setRings(getCoordinateRings(polygon, saveCoordinatesIndividually, countryIsoAlpha3Code, level));

        return p;
    }

    private Set<CoordinateRing> getCoordinateRings(com.vividsolutions.jts.geom.Polygon polygon, boolean saveCoordinatesIndividually,
                                                   String countryIsoAlpha3Code, Integer level) {

        Set<CoordinateRing> list = new LinkedHashSet<>();

        list.add(getCoordinateRing(polygon.getExteriorRing(), CoordinateRingType.OUTER, saveCoordinatesIndividually));

        if (polygon.getNumInteriorRing() > 0) {
            for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                list.add(getCoordinateRing(polygon.getInteriorRingN(i), CoordinateRingType.INNER, saveCoordinatesIndividually));
            }
        }

        return list;
    }

    private CoordinateRing getCoordinateRing(
            com.vividsolutions.jts.geom.LineString lineString, CoordinateRingType type, boolean saveCoordinatesIndividually) {

        Set<Coordinate> coordinates = getCoordinates(lineString.getCoordinates());

        EncodedCoordinatesString encodedCoordinatesString = new EncodedCoordinatesString();
        encodedCoordinatesString.setValue(generateEncodedCoordinatesString(coordinates));

        CoordinateRing ring = new CoordinateRing();

        ring.setType(type);
        ring.setEncodedCoordinatesString(encodedCoordinatesString);

        if (saveCoordinatesIndividually) {
            ring.setCoordinates(coordinates);
        }

        setMinMaxCoordinates(ring, coordinates);

        return ring;
    }

    private Set<Coordinate> getCoordinates(com.vividsolutions.jts.geom.Coordinate[] coordinates) {

        Set<Coordinate> coordinateList = new LinkedHashSet<>();
        int orderNo = 0;
        Coordinate coordinate;

        for (com.vividsolutions.jts.geom.Coordinate c : coordinates) {
            orderNo++;
            coordinate = new Coordinate();
            coordinate.setOrderNo(orderNo);
            coordinate.setLongitude(new BigDecimal(c.x));
            coordinate.setLatitude(new BigDecimal(c.y));
            coordinateList.add(coordinate);
        }

        return coordinateList;
    }

    public List<PolygonRegion> findAnyLevelPolygonRegionThatContainsPoint(BigDecimal latitude, BigDecimal longitude) {

        List<PolygonRegion> resultList = new ArrayList<>();

        PolygonRegion polygonRegion = findLowestLevelPolygonRegionThatContainsPoint(latitude, longitude);

        if (polygonRegion != null) {
            resultList.add(polygonRegion);
            resultList.addAll(getAllParentPolygonRegions(polygonRegion));
        }

        return resultList;
    }

    public PolygonRegion findLowestLevelPolygonRegionThatContainsPoint(BigDecimal latitude, BigDecimal longitude) {

        PolygonRegion result = null;

        List<PolygonRegion> list = polygonRegionRepository.findAllForCoordinateSearchDistinctByLevel(0);

        for (PolygonRegion pr : list) {
            result = findLowestLevelPolygonRegionThatContainsPoint(pr, latitude, longitude);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    public PolygonRegion findLowestLevelPolygonRegionThatContainsPoint(PolygonRegion polygonRegion, BigDecimal latitude, BigDecimal longitude) {

        PolygonRegion result = null;

        boolean mayPolygonRegionContainPoint = mayPolygonRegionContainPoint(polygonRegion, latitude, longitude);

        if (mayPolygonRegionContainPoint) {

            if (!polygonRegion.getHasChildren()) {

                if (doesPolygonRegionContainPoint(polygonRegion, latitude, longitude)) {
                    result = polygonRegion;
                }

            } else {

                for (PolygonRegion pr : polygonRegionRepository.findAllForCoordinateSearchDistinctByParent(getPolygonRegionAbsoluteName(polygonRegion))) {
                    result = findLowestLevelPolygonRegionThatContainsPoint(pr, latitude, longitude);
                    if (result != null) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    private boolean mayPolygonRegionContainPoint(PolygonRegion polygonRegion, BigDecimal latitude, BigDecimal longitude) {

        boolean result = false;

        for (Polygon p : polygonRegion.getPolygons()) {

            for (CoordinateRing cr : p.getRings()) {

                // Verilen nokta, dış koordinat çemberini içeren dikdörtgen içinde mi?
                if (cr.getType().equals(CoordinateRingType.OUTER)) {

                    boolean latitudeIsInRange = (latitude.compareTo(cr.getMinLatitude()) >= 0 && latitude.compareTo(cr.getMaxLatitude()) <= 0);
                    boolean longitudeIsInRange = (longitude.compareTo(cr.getMinLongitude()) >= 0 && longitude.compareTo(cr.getMaxLongitude()) <= 0);

                    if (latitudeIsInRange && longitudeIsInRange) {
                        result = true;
                    }

                    // Bir poligonda sadece bir adet dış koordinat çemberi olduğu için diğerlerine bakmaya gerek yok.
                    break;
                }
            }

            if (result) {
                break;
            }
        }

        return result;
    }

    private boolean doesPolygonRegionContainPoint(PolygonRegion polygonRegion, BigDecimal latitude, BigDecimal longitude) {

        boolean result = false;

        com.vividsolutions.jts.geom.GeometryFactory geometryFactory = new com.vividsolutions.jts.geom.GeometryFactory();

        com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(
                new com.vividsolutions.jts.geom.Coordinate(longitude.doubleValue(), latitude.doubleValue()));

        for (Polygon p : polygonRegion.getPolygons()) {

            CoordinateRing outerRing = null;
            List<CoordinateRing> innerRings = new ArrayList<>();

            for (CoordinateRing cr : p.getRings()) {

                if (cr.getType().equals(CoordinateRingType.OUTER)) {
                    outerRing = cr;
                } else {
                    innerRings.add(cr);
                }
            }

            com.vividsolutions.jts.geom.LinearRing shell = createLinearRing(geometryFactory, outerRing);
            com.vividsolutions.jts.geom.LinearRing[] holes = new com.vividsolutions.jts.geom.LinearRing[innerRings.size()];

            for (int i = 0; i < innerRings.size(); i++) {
                holes[i] = createLinearRing(geometryFactory, innerRings.get(i));
            }

            com.vividsolutions.jts.geom.Polygon polygon = null;

            if (holes.length == 0) {
                polygon = geometryFactory.createPolygon(shell);
            } else {
                polygon = geometryFactory.createPolygon(shell, holes);
            }

            if (polygon.contains(point)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private boolean doesPolygonRegionContainPoint_method2(PolygonRegion polygonRegion, BigDecimal latitude, BigDecimal longitude) {

        boolean result = false;

        for (Polygon p : polygonRegion.getPolygons()) {

            CoordinateRing outerRing = null;
            List<CoordinateRing> innerRings = new ArrayList<>();

            for (CoordinateRing cr : p.getRings()) {

                if (cr.getType().equals(CoordinateRingType.OUTER)) {
                    outerRing = cr;
                } else {
                    innerRings.add(cr);
                }
            }

            ekol.location.domain.sromku.Polygon.Builder polygonBuilder = ekol.location.domain.sromku.Polygon.Builder();

            for (Coordinate c : coordinateRepository.findAllByRingIdOrderByOrderNo(outerRing.getId())) {
                polygonBuilder.addVertex(new ekol.location.domain.sromku.Point(c.getLongitude().floatValue(), c.getLatitude().floatValue()));
            }

            polygonBuilder.close();

            for (CoordinateRing cr : innerRings) {
                for (Coordinate c : coordinateRepository.findAllByRingIdOrderByOrderNo(cr.getId())) {
                    polygonBuilder.addVertex(new ekol.location.domain.sromku.Point(c.getLongitude().floatValue(), c.getLatitude().floatValue()));
                }
                polygonBuilder.close();
            }

            if (polygonBuilder.build().contains(new ekol.location.domain.sromku.Point(longitude.floatValue(), latitude.floatValue()))) {
                result = true;
                break;
            }
        }

        return result;
    }

    private com.vividsolutions.jts.geom.LinearRing createLinearRing(
            com.vividsolutions.jts.geom.GeometryFactory geometryFactory, CoordinateRing ring) {

        List<com.vividsolutions.jts.geom.Coordinate> coordinates = new ArrayList<>();

        for (Coordinate c : coordinateRepository.findAllByRingIdOrderByOrderNo(ring.getId())) {
            coordinates.add(new com.vividsolutions.jts.geom.Coordinate(c.getLongitude().doubleValue(), c.getLatitude().doubleValue()));
        }

        return geometryFactory.createLinearRing(coordinates.toArray(new com.vividsolutions.jts.geom.Coordinate[coordinates.size()]));
    }

    public String getPolygonRegionAbsoluteName(PolygonRegion polygonRegion) {
        if (polygonRegion.getParent().equals("/")) {
            return "/" + polygonRegion.getName();
        } else {
            return polygonRegion.getParent() + "/" + polygonRegion.getName();
        }
    }

    public PolygonRegion getParentPolygonRegion(PolygonRegion polygonRegion) {

        if (polygonRegion.getParent().equals("/")) {
            return null;
        } else {
            String[] queryParams = getQueryParamsForParentPolygonRegionQuery(polygonRegion);
            String queryParamParent = queryParams[0];
            String queryParamName = queryParams[1];
            return polygonRegionRepository.findByParentAndName(queryParamParent, queryParamName);
        }
    }

    public List<PolygonRegion> getAllParentPolygonRegions(PolygonRegion polygonRegion) {

        List<PolygonRegion> resultList = new ArrayList<>();

        PolygonRegion currentPolygonRegion = polygonRegion;
        PolygonRegion parentPolygonRegion;

        while ((parentPolygonRegion = getParentPolygonRegion(currentPolygonRegion)) != null) {
            resultList.add(parentPolygonRegion);
            currentPolygonRegion = parentPolygonRegion;
        }

        return resultList;
    }

    /**
     * Verilen polygonRegion'ın parent'ını bulabilmek için gerekli olan sorgu parametrelerini çıkaran metod.
     */
    public String[] getQueryParamsForParentPolygonRegionQuery(PolygonRegion polygonRegion) {

        if (!polygonRegion.getHasParent()) {
            throw new IllegalArgumentException("This polygon region does not have a parent.");
        } else {

            String queryParamParent = polygonRegion.getParent().substring(0, polygonRegion.getParent().lastIndexOf("/"));

            if (queryParamParent.length() == 0) {
                queryParamParent = "/";
            }

            String queryParamName = polygonRegion.getParent().substring(polygonRegion.getParent().lastIndexOf("/") + 1);

            return new String[] {queryParamParent, queryParamName};
        }
    }
}
