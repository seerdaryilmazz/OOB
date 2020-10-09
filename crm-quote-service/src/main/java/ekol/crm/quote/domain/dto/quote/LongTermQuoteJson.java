package ekol.crm.quote.domain.dto.quote;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.businessVolume.BusinessVolumeJson;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.ValidationException;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LongTermQuoteJson extends QuoteJson{

    private LocalDate closeDate;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate operationStartDate;
    private MonetaryAmountJson totalPrice;
    private MonetaryAmountJson totalExpectedTurnover;
    private BusinessVolumeJson businessVolume;

    public Quote toEntity(){
        LongTermQuote entity = new LongTermQuote();
        entity.setId(getId());
        entity.setNumber(getNumber());
        entity.setName(getName());
        entity.setAccount(getAccount());
        entity.setAccountLocation(getAccountLocation());
        entity.setSubsidiary(getSubsidiary());
        entity.setType(getType());
        entity.setServiceArea(getServiceArea().getCode());
        entity.setStatus(getStatus());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setQuoteOwner(getQuoteOwner());
        entity.setCloseDate(getCloseDate());
        entity.setContractStartDate(getContractStartDate());
        entity.setContractEndDate(getContractEndDate());
        entity.setOperationStartDate(getOperationStartDate());
        entity.setProducts(Optional.ofNullable(getProducts()).map(Collection::stream).orElseGet(Stream::empty).parallel().map(ProductJson::toEntity).collect(Collectors.toSet()));
        entity.setTotalPrice(getTotalPrice() != null ? getTotalPrice().toEntity(): null);
        entity.setTotalExpectedTurnover(getTotalExpectedTurnover() != null ? getTotalExpectedTurnover().toEntity(): null);
        entity.setNotes(Optional.ofNullable(getNotes()).map(Collection::stream).orElseGet(Stream::empty).map(NoteJson::toEntity).collect(Collectors.toList()));
        entity.setDocuments(Optional.ofNullable(getDocuments()).map(Collection::stream).orElseGet(Stream::empty).map(DocumentJson::toEntity).collect(Collectors.toList()));
        entity.setDefaultInvoiceCompanyCountry(getDefaultInvoiceCompanyCountry());
        entity.setHoldingForCompanyTransfer(getHoldingForCompanyTransfer());
        entity.setRequestedDate(getRequestedDate());
        entity.getQuoteAttribute().putAll(getQuoteAttribute());
        entity.getOrders().addAll(getOrders().stream().map(QuoteOrderMappingJson::toEntity).collect(Collectors.toSet()));
        entity.setBusinessVolume(Optional.ofNullable(getBusinessVolume()).map(BusinessVolumeJson::toEntity).orElse(null));

        
        if(getStatus() == QuoteStatus.CLOSED || getStatus() == QuoteStatus.CANCELED){
        	if(!CollectionUtils.isEmpty(getProducts())) {
        		boolean allProductsWon = getProducts().stream().allMatch(product -> product.getStatus() == ProductStatus.WON);
	            if (allProductsWon) {
	                entity.setStatus(QuoteStatus.WON);
	            }else{
	                boolean wonProductsExits = getProducts().stream().anyMatch(product -> product.getStatus() == ProductStatus.WON);
	                if(wonProductsExits){
	                    entity.setStatus(QuoteStatus.PARTIAL_WON);
	                }else if(getProducts().stream().allMatch(product -> product.getStatus() == ProductStatus.LOST)){
	                    entity.setStatus(QuoteStatus.LOST);
	                }
	            }
	            boolean productsCanceled= getProducts().stream().anyMatch(product->product.getStatus()==ProductStatus.CANCELED);
	            if(productsCanceled) {
	            	entity.setStatus(QuoteStatus.CANCELED);
	            }
            }
            if(QuoteStatus.CANCELED != entity.getStatus()) {
            	if(CollectionUtils.isEmpty(getProducts())) {
            		throw new ValidationException("At least one product should be entered");
            	}
            	if(getProducts().stream().anyMatch(quoteProduct -> quoteProduct.getStatus() == null)){
            		throw new ValidationException("Each product should have status");
            	}

            	if(!getProducts().stream().allMatch(product -> product.getStatus() == ProductStatus.WON || product.getStatus() == ProductStatus.LOST || product.getStatus() == ProductStatus.CANCELED)){
            		throw new ValidationException("Each product status should be marked as won or lost");
            	}
            }
            
            if(entity.getStatus() != QuoteStatus.LOST && entity.getStatus() != QuoteStatus.CANCELED){
                if(getContractStartDate() == null){
                    throw new ValidationException("Contract start date should not be empty");
                }
                if(getContractEndDate() == null){
                    throw new ValidationException("Contract end date should not be empty");
                }
                if(getOperationStartDate() == null){
                    throw new ValidationException("Operation start date should not be empty");
                }
                if(getContractEndDate().isBefore(getContractStartDate())){
                    throw new ValidationException("Contract end date should not be before than contract start date");
                }
            }
        }
        return entity;
    }
    public void validate(QuoteType type){

        if(getCloseDate() == null){
            throw new ValidationException("Close date should not be empty");
        }
        if(getContractStartDate() != null && getContractEndDate() != null){
            if(getContractEndDate().isBefore(getContractStartDate())){
                throw new ValidationException("Contract end date should not be before than contract start date");
            }
        }
        if(!CollectionUtils.isEmpty(getProducts())){
            getProducts().parallelStream().forEach(product -> product.validateProduct(type, getDefaultInvoiceCompanyCountry()));
        }
    }
}

