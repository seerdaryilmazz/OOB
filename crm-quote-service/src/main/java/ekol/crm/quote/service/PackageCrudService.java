package ekol.crm.quote.service;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.model.Package;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.PackageRepository;
import ekol.crm.quote.validator.PackageValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PackageCrudService {

    private PackageRepository repository;
    private PackageValidator validator;

    @Transactional
    public Set<Package> save(Quote quote, Set<Package> existing, Set<Package> request){

        Set<Package> savedPackages = new HashSet<>();
        if(!CollectionUtils.isEmpty(request)){
            Set<Package> packages = request.stream().map(p -> {
                p.setQuote(quote);
                this.validator.validate(p);
                if (p.getType().equals(Constants.STANGE_PACKAGE_TYPE_CODE)) {
                    p.setStackabilityType(null);
                } else {
                    p.setHangingGoodsCategory(null);
                }
                return p;
            }).collect(Collectors.toSet());

            savedPackages.addAll(IteratorUtils.toList(this.repository.save(packages).iterator()));
        }
        // delete discarded packages
        deleteDiscardedPackages(existing, savedPackages);

        return savedPackages;
    }

    private void deleteDiscardedPackages(Set<Package> existing, Set<Package> savedPackages) {
        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedPackageIds = savedPackages.stream().map(Package::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedPackageIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(Package quotePackage){
        quotePackage.setDeleted(true);
        repository.save(quotePackage);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<Package> packages = repository.findByQuote(quote);
        packages.forEach(p -> {
            p.setDeleted(true);
            repository.save(p);
        });
    }
}





