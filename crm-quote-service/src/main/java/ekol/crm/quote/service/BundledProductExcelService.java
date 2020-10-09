package ekol.crm.quote.service;

import java.io.File;
import java.math.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import ekol.crm.quote.client.*;
import ekol.crm.quote.domain.dto.accountservice.*;
import ekol.crm.quote.domain.dto.orderservice.Incoterm;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.enumaration.Currency;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.*;
import ekol.exceptions.*;
import ekol.model.*;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BundledProductExcelService {

    private OrderService orderService;
    private AccountService accountService;

	private static HashMap<Integer,String> map =new HashMap<>();

	@SuppressWarnings("Duplicates")
	public Set<ProductJson> processBundledProductListExcel(File file, CodeNamePair serviceArea, CountryPointType countryPointType) {
		if (!Objects.equals("ROAD", serviceArea.getCode())) {
			throw new BadRequestException("This operation is supported for only service area 'Road'.");
		}

		map.put(0, "FROM COUNTRY");
		map.put(1, "FROM POINT");
		map.put(2, "TO COUNTRY");
		map.put(3, "TO POINT");
		map.put(4, "EXISTENCE TYPE");
		map.put(5, "SHIPMENT LOADING TYPE");
		map.put(6, "UNIT OF MEASURE");
		map.put(7, "QUANTITY");
		map.put(8, "PRICE");
		map.put(9, "EXPECTED TURNOVER");
		map.put(10, "CURRENCY");
		map.put(11, "INCOTERM");
		map.put(12, "STATUS");
		map.put(13, "LOST_REASON");

		List<List<Object>> rowValues = null;
		try {
			rowValues = ekol.excel.Utils.extractData(file, 0, 2, -1, 0, 13);
		} catch (Exception e) {
			throw new ApplicationException("Could not extract data.", e);
		}

		int fromCountryCellIndex = 0;
		int fromCountryPointCellIndex = 1;
		int toCountryCellIndex = 2;
		int toCountryPointCellIndex = 3;
		int existenceTypeCellIndex = 4;
		int shipmentLoadingTypeCellIndex = 5;
		int unitOfMeasureCellIndex = 6;
		int quantityCellIndex = 7;
		int priceCellIndex = 8;
		int expectedTurnoverCellIndex = 9;
		int currencyCellIndex = 10;
		int incotermCellIndex = 11;
		int statusCellIndex = 12;
		int lostReasonCellIndex = 13;
		Set<Product> productsSet = new HashSet<>();
		Set<String> shipmentLoadingTypesSuitableForSpecifiedServiceArea = findShipmentLoadingTypesSuitableForServiceArea(serviceArea);
		int initialRowIndex = 0;
		for (int i = initialRowIndex; i < rowValues.size(); i++) {

			List<Object> rowValue = rowValues.get(i);

			if(!rowValue.stream().allMatch(Objects::isNull)){
				Country fromCountry;
				try {
					fromCountry = getCellValue(rowValue, fromCountryCellIndex, true, k->accountService.findCountry(k, false), i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, fromCountryCellIndex)), e);
					}
				}

				CountryPoint fromCountryPoint;
				try {
					fromCountryPoint = getCellValue(rowValue, fromCountryPointCellIndex, false, k->accountService.findCountryPointByCountryName(fromCountry.getName(), countryPointType, k, true), i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, fromCountryPointCellIndex)), e);
					}
				}

				Country toCountry;
				try {
					toCountry = getCellValue(rowValue, toCountryCellIndex, true, k->accountService.findCountry(k, false), i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, toCountryCellIndex)), e);
					}
				}

				CountryPoint toCountryPoint;
				try {
					toCountryPoint = getCellValue(rowValue, toCountryPointCellIndex, false, k->accountService.findCountryPointByCountryName(toCountry.getName(), countryPointType, k, true), i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, toCountryPointCellIndex)), e);
					}
				}

				ExistenceType existenceType;
				try {
					existenceType = getCellValue(rowValue, existenceTypeCellIndex, true, ExistenceType::valueOf, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, existenceTypeCellIndex)), e);
					}
				}

				String shipmentLoadingType = null;
				if (Stream.of("ROAD","SEA").anyMatch(serviceArea.getCode()::equals)) {
					try {
						shipmentLoadingType = getCellValue(rowValue, shipmentLoadingTypeCellIndex, true, String::valueOf, i);
					} catch (Exception e) {
						if(e.getMessage().contains("must be filled")){
							throw e;
						}else {
							throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, shipmentLoadingTypeCellIndex)), e);
						}
					}
				}

				UnitOfMeasure unitOfMeasure;
				try {
					unitOfMeasure = getCellValue(rowValue, unitOfMeasureCellIndex, true, UnitOfMeasure::valueOf, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, unitOfMeasureCellIndex)), e);
					}
				}

				Integer quantity;
				try {
					quantity = getCellValue(rowValue, quantityCellIndex, true, Integer::valueOf, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, quantityCellIndex)), e);
					}
				}

				BigDecimal price = null;
				if (!Objects.equals("LTL", shipmentLoadingType)) {
					try {
						price = getCellValue(rowValue, priceCellIndex, false, BigDecimal::new, i);
					} catch (Exception e) {
						if(e.getMessage().contains("must be filled")){
							throw e;
						}else {
							throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, priceCellIndex)), e);
						}
					}
				}

				BigDecimal expectedTurnover;
				try {
					expectedTurnover = getCellValue(rowValue, expectedTurnoverCellIndex, true, BigDecimal::new, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, expectedTurnoverCellIndex)), e);
					}
				}

				Currency currency;
				try {
					currency = getCellValue(rowValue, currencyCellIndex, true, Currency::valueOf, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, currencyCellIndex)), e);
					}
				}

				Incoterm incoterm;
				try {
					incoterm = getCellValue(rowValue, incotermCellIndex, false, k->orderService.findIncoterm(k, true), i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, incotermCellIndex)), e);
					}
				}

				ProductStatus productStatus;
				try {
					productStatus = getCellValue(rowValue, statusCellIndex, false, ProductStatus::valueOf, i);
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, statusCellIndex)), e);
					}
				}

				LostReasonType lostReason = null;
				try {
					if(ProductStatus.LOST == productStatus) {
						lostReason = getCellValue(rowValue, lostReasonCellIndex, true, LostReasonType::valueOf, i);
					}
				} catch (Exception e) {
					if(e.getMessage().contains("must be filled")){
						throw e;
					}else {
						throw new BadRequestException(MessageFormat.format("{0} includes invalid cell. Please check row {1}, column {2}.", adjustColumnNameAndRowAndCellIndex(initialRowIndex, i, lostReasonCellIndex)), e);
					}
				}

				if(!Optional.ofNullable(shipmentLoadingType).map(shipmentLoadingTypesSuitableForSpecifiedServiceArea::contains).orElse(true)) {
					throw new BadRequestException(MessageFormat.format("Shipment loading type is not suitable for specified service area. Check row {0}, cell {1}.", adjustRowAndCellIndex(initialRowIndex, i, shipmentLoadingTypeCellIndex)));
				}

				if (unitOfMeasure.getScope().stream().noneMatch(Optional.ofNullable(shipmentLoadingType).orElse("UNDEFINED")::equals)) {
					throw new BadRequestException(MessageFormat.format("Unit of measure is not suitable for specified shipment loading type. Check row {0}, cell {1}.", adjustRowAndCellIndex(initialRowIndex, i, unitOfMeasureCellIndex)));
				}
				productsSet.add(createBundledProduct(
						serviceArea, fromCountry, fromCountryPoint, toCountry, toCountryPoint, existenceType,
						shipmentLoadingType, unitOfMeasure, quantity, price, expectedTurnover, currency, incoterm,productStatus,lostReason ));
			}
		}
		return productsSet.stream().map(Product::toJson).collect(Collectors.toSet());
	}

    public Set<String> findShipmentLoadingTypesSuitableForServiceArea(CodeNamePair serviceArea) {

        Set<String> shipmentLoadingTypes = new HashSet<>();
        List<ShipmentLoadingType> shipmentLoadingTypeObjects = accountService.findShipmentLoadingTypes(serviceArea.getCode());

        /**
         * Normalde crm-account-service'teki ShipmentLoadingType sınıfında LTL diye bir seçenek yok ama
         * serviceArea = ROAD olduğunda arayüzde LTL_GROUPAGE ve LTL_PART_LOAD gösterilmiyormuş, bunların yerine LTL diye
         * bir seçenek gösteriliyormuş.
         */

        for (ShipmentLoadingType item : shipmentLoadingTypeObjects) {
            if (!serviceArea.getCode().equals("ROAD") || !item.getId().startsWith("LTL")) {
                shipmentLoadingTypes.add(item.getId());
            }
        }

        if (serviceArea.getCode().equals("ROAD")) {
            shipmentLoadingTypes.add("LTL");
        }

        return shipmentLoadingTypes;
    }

    /**
     * @return Satır ve hücre numarasını, kullanıcının anlayacağı hale çevirir.
     */
    public Object[] adjustRowAndCellIndex(int initialRowIndex, int currentRowIndex, int currentCellIndex) {
        return new Object[] { Integer.toString(currentRowIndex - initialRowIndex + 3), Integer.toString(currentCellIndex + 1)};
    }

	/**
	 * @return Satır adı, numarası ve hücre numarasını, kullanıcının anlayacağı hale çevirir.
	 */
	public Object[] adjustColumnNameAndRowAndCellIndex(int initialRowIndex, int currentRowIndex, int currentCellIndex) {
		return new Object[]{map.get(currentCellIndex), Integer.toString(currentRowIndex - initialRowIndex + 3), Integer.toString(currentCellIndex + 1)};
	}

    public Object getSafe(List<Object> list, int index) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

	public <R> R getCellValue(List<Object> rowValue, int cellIndex, boolean isRequired, Function<String, R> function, int currentRowIndex){
		Object obj = getSafe(rowValue, cellIndex);
		if (Objects.isNull(obj)) {
			if(isRequired) {
				throw new BadRequestException(MessageFormat.format("{0} cells must be filled. Please check row {1}, column {2}!", adjustColumnNameAndRowAndCellIndex(0, currentRowIndex, cellIndex)));
			}
			return null;
		}
		
		R result = null;
		if (obj instanceof String) {
			result = function.apply(String.class.cast(obj).trim());
		} else if(obj instanceof BigDecimal) {
			BigDecimal value = BigDecimal.class.cast(obj);
			Long longValue = value.longValue();
			if(0 > BigDecimal.ZERO.compareTo(value.subtract(BigDecimal.valueOf(longValue)))) {
				result = function.apply(value.setScale(6, RoundingMode.HALF_UP).toPlainString());
			} else {
				result = function.apply(String.valueOf(longValue).trim());
			}
		}
		return result;
	}

	public BundledProduct createBundledProduct(
			CodeNamePair serviceArea, Country fromCountry, CountryPoint fromCountryPoint, Country toCountry, CountryPoint toCountryPoint,
			ExistenceType existenceType, String shipmentLoadingType, UnitOfMeasure unitOfMeasure, Integer quantity,
			BigDecimal price, BigDecimal expectedTurnover, Currency currency, Incoterm incoterm,ProductStatus productStatus,LostReasonType reason) {

		BundledProduct bp = new BundledProduct();
		Optional.ofNullable(serviceArea).map(CodeNamePair::getCode).ifPresent(bp::setServiceArea);
		Optional.ofNullable(fromCountry).map(t->new IsoNamePair(t.getIso(), t.getName())).ifPresent(bp::setFromCountry);
		Optional.ofNullable(fromCountryPoint).map(t->new IdNamePair(t.getId(), t.getName())).ifPresent(bp::setFromPoint);
		Optional.ofNullable(toCountry).map(t->new IsoNamePair(t.getIso(), t.getName())).ifPresent(bp::setToCountry);
		Optional.ofNullable(toCountryPoint).map(t->new IdNamePair(t.getId(), t.getName())).ifPresent(bp::setToPoint);
		Optional.ofNullable(existenceType).ifPresent(bp::setExistence);
		Optional.ofNullable(shipmentLoadingType).ifPresent(bp::setShipmentLoadingType);
		Optional.ofNullable(unitOfMeasure).ifPresent(bp::setUnitOfMeasure);
		Optional.ofNullable(quantity).ifPresent(bp::setQuantity);
		Optional.ofNullable(price).map(t->new MonetaryAmount(t, currency)).ifPresent(bp::setPriceOriginal);
		Optional.ofNullable(expectedTurnover).map(t->new MonetaryAmount(t, currency)).ifPresent(bp::setExpectedTurnoverOriginal);
		Optional.ofNullable(incoterm).map(Incoterm::getCode).ifPresent(bp::setIncoterm);
		bp.setStatus(Optional.ofNullable(productStatus).orElse(ProductStatus.OPEN));
		if(ProductStatus.LOST == productStatus) {
			bp.setLostReason(Optional.ofNullable(reason).map(t->LostReason.builder().reason(reason).build()).orElseThrow(()->new BadRequestException("No value")));
		}
		return bp;
	}
}
