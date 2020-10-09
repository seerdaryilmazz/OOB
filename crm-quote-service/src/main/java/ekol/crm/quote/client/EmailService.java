package ekol.crm.quote.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.EmailMessage;

@Service
public class EmailService {
	@Value("${oneorder.email-service}")
	private String emailServiceName;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String sendMail(EmailMessage message) {
		return restTemplate.postForObject(emailServiceName + "/send", message, String.class);
	}
}
