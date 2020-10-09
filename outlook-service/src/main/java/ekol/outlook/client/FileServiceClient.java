package ekol.outlook.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FileServiceClient {

	@Value("${oneorder.file-service}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public byte[] download(String id) {
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		return restTemplate.getForObject(url + "/{id}/download", byte[].class, id);
	}
}
