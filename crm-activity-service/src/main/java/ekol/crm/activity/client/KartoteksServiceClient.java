package ekol.crm.activity.client;

import java.util.*;
import java.util.stream.Stream;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.crm.activity.client.dto.*;
import ekol.exceptions.ResourceNotFoundException;

@Component
public class KartoteksServiceClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(KartoteksServiceClient.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${oneorder.kartoteks-service}")
	private String kartoteksService;
	
	public Contact getContact(Long id) {
		try {
			return restTemplate.getForObject(kartoteksService + "/contact/{id}", Contact.class, id);
		} catch(HttpClientErrorException e){
			LOGGER.error("Contact not found. id: {}", id);
			throw e;
		}
	}
	
	public Contact getContact(Long companyId, Long contactId) {
		try {
			Company company = restTemplate.getForObject(kartoteksService + "/company/{companyId}", Company.class, companyId);
			return Optional.ofNullable(company)
					.map(Company::getCompanyContacts)
					.map(Collection::stream)
					.orElseGet(Stream::empty)
					.filter(contact->Objects.equals(contact.getId(), contactId))
					.findFirst()
					.orElseThrow(ResourceNotFoundException::new);
		} catch(HttpClientErrorException e){
			LOGGER.error("Company not found. id: {}", companyId);
			throw e;
		}catch(ResourceNotFoundException e){
			LOGGER.error("Contact not found. id: {}", contactId);
			throw e;
		}

	}


}
