package ekol.crm.quote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.model.OwnerCompany;
import ekol.crm.quote.repository.OwnerCompanyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerCompanyService {
	
	OwnerCompanyRepository ownerCompanyRepository;
	
	public List<OwnerCompany> getByType(String type) {
		
		return ownerCompanyRepository.findByType(type);
	}

	
}
