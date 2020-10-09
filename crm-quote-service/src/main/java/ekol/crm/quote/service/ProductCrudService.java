package ekol.crm.quote.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.domain.model.product.Product;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.ProductRepository;
import ekol.crm.quote.validator.ProductValidator;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductCrudService {

    private ProductRepository repository;
    private ProductValidator validator;
    private LostReasonCrudService lostReasonCrudService;

    @Transactional
    public Set<Product> save(Quote quote, Set<Product> existed, Set<Product> request){
    	Set<Product> products = request.stream().map(product->this.saveOrUpdate(quote, product)).collect(Collectors.toSet());
    	Set<Long> deleteIds = SetUtils.difference(
    			existed.parallelStream().map(Product::getId).collect(Collectors.toSet()), 
    			request.parallelStream().map(Product::getId).collect(Collectors.toSet())
    			);
    	this.delete(existed.parallelStream().filter(product->deleteIds.contains(product.getId())).collect(Collectors.toSet()));
    	return products;
    }
    
    public Product saveOrUpdate(Quote quote, Product product) {
    	product.setQuote(quote);
		this.validator.validate(product);
		this.lostReasonCrudService.save(product.getLostReason());
		return repository.save(product);
    }

    public Product getByIdOrThrowException(Long id) {
        return this.repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product with id {0} not found", id)
        );
    }

    @Transactional
    public void delete(Product product){
        product.setDeleted(true);
        repository.save(product);
    }

    @Transactional
    public void delete(Set<Product> products){
    	products.forEach(t->t.setDeleted(Boolean.TRUE));
    	repository.save(products);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<Product> products = repository.findByQuote(quote);
        products.forEach(this::delete);
    }
}
