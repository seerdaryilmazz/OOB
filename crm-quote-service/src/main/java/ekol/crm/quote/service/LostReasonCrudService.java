package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.LostReason;
import ekol.crm.quote.repository.LostReasonRepository;
import ekol.crm.quote.validator.LostReasonValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LostReasonCrudService {

    private LostReasonRepository repository;
    private LostReasonValidator validator;

    @Transactional
    public LostReason save(LostReason request){
        LostReason savedLostReason = null;
        if(request != null){
            this.validator.validate(request);
            savedLostReason = repository.save(request);
        }
        return savedLostReason;
    }

    @Transactional
    public void delete(LostReason lostReason){
        lostReason.setDeleted(true);
        repository.save(lostReason);
    }
}
