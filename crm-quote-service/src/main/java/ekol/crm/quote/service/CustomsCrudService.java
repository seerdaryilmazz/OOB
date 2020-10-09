package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.Customs;
import ekol.crm.quote.repository.CustomsRepository;
import ekol.crm.quote.validator.CustomsValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomsCrudService {

    private CustomsRepository repository;
    private CustomsValidator validator;

    @Transactional
    public Customs save(Customs request){
        if(ObjectUtils.isEmpty(request)){
            return null;
        }
        this.validator.validate(request);
        return repository.save(request);
    }

    @Transactional
    public void delete(Customs quoteCustoms){
        quoteCustoms.setDeleted(true);
        repository.save(quoteCustoms);
    }
}
