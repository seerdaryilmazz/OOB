package ekol.crm.quote.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import ekol.crm.quote.domain.model.quote.LongTermQuote;
import ekol.crm.quote.repository.BusinessVolumeRepository;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessVolumeService {
	
	private BusinessVolumeRepository businessVolumeRepository;
	
	
	public BusinessVolume getByIdOrThrowException(Long id) {
		 return this.businessVolumeRepository.findById(id).orElseThrow(() ->
		 	new ResourceNotFoundException("Business Volume with id {0} not found", id)
	);
	}
	
    @Transactional
    public BusinessVolume save(LongTermQuote existed, BusinessVolume request){
    	if(Objects.isNull(request)) {
    		BusinessVolume existedBv = existed.getBusinessVolume();
    		if(Objects.nonNull(existedBv)) {
    			existedBv.setDeleted(true);
    			this.businessVolumeRepository.save(existedBv);
    		}
        	return null;
    		
    	}
		return this.businessVolumeRepository.save(request);
    }

}
