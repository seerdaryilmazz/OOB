package ekol.crm.quote.util;

import java.util.Objects;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.model.product.Product;

public class ProductUtils {
	public static boolean isImport(ProductJson product) {
		return !Objects.equals(Constants.COUNTRY_CODE_TR, product.getFromCountry().getIso()) 
				&& Objects.equals(Constants.COUNTRY_CODE_TR, product.getToCountry().getIso());
	
	}
	
	public static boolean isExport(ProductJson product) {
		return Objects.equals(Constants.COUNTRY_CODE_TR, product.getFromCountry().getIso()) 
				&& !Objects.equals(Constants.COUNTRY_CODE_TR, product.getToCountry().getIso());
	}

	public static boolean isImport(Product product) {
		return !Objects.equals(Constants.COUNTRY_CODE_TR, product.getFromCountry().getIso()) 
				&& Objects.equals(Constants.COUNTRY_CODE_TR, product.getToCountry().getIso());
		
	}
	
	public static boolean isExport(Product product) {
		return Objects.equals(Constants.COUNTRY_CODE_TR, product.getFromCountry().getIso()) 
				&& !Objects.equals(Constants.COUNTRY_CODE_TR, product.getToCountry().getIso());
	}
}
