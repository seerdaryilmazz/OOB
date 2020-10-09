package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.Price;
import ekol.crm.quote.domain.model.PriceDiscount;
import ekol.crm.quote.repository.PriceDiscountRepository;
import ekol.crm.quote.validator.PriceDiscountValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PriceDiscountCrudService {

    private PriceDiscountRepository repository;
    private PriceDiscountValidator validator;

    @Transactional
    public List<PriceDiscount> save(Price price, List<PriceDiscount> request){

        List<PriceDiscount> savedPriceDiscounts;
        if(CollectionUtils.isEmpty(request)){
            savedPriceDiscounts = new ArrayList<>();
        }else{
            List<PriceDiscount> priceDiscounts = request.stream().map(priceDiscount -> {
                priceDiscount.setPrice(price);
                this.validator.validate(priceDiscount);
                return priceDiscount;
            }).collect(Collectors.toList());

            savedPriceDiscounts = IteratorUtils.toList(this.repository.save(priceDiscounts).iterator());
            if(price.getPriceCalculation() != null){
                price.getPriceCalculation().setDiscounts(savedPriceDiscounts);
            }
        }
        // delete discarded price discounts
        List<PriceDiscount> discardedPriceDiscounts = determineDiscardedPriceDiscounts(price, savedPriceDiscounts);

        discardedPriceDiscounts.forEach(priceDiscount -> delete(priceDiscount));

        return savedPriceDiscounts;
    }

    private List<PriceDiscount> determineDiscardedPriceDiscounts(Price price, List<PriceDiscount> savedPriceDiscounts) {
        Set<Long> savedPriceDiscountIds = savedPriceDiscounts.stream().map(PriceDiscount::getId)
                .collect(Collectors.toSet());

        List<PriceDiscount> existingPriceDiscounts = repository.findByPrice(price);

        return existingPriceDiscounts.stream().filter(c ->
                !savedPriceDiscountIds.contains(c.getId())).collect(Collectors.toList());
    }

    @Transactional
    public void delete(PriceDiscount priceDiscount){
        priceDiscount.setDeleted(true);
        repository.save(priceDiscount);
    }


    public List<PriceDiscount> listForPrice(Price price){
        return repository.findByPrice(price);
    }

    @Transactional
    public void deleteForQuotePrice(Price price){
        if(price.getPriceCalculation() != null &&
                !CollectionUtils.isEmpty(price.getPriceCalculation().getDiscounts()))
            price.getPriceCalculation().getDiscounts().forEach(priceDiscount -> {
                priceDiscount.setDeleted(true);
                repository.save(priceDiscount);
        });
    }
}
