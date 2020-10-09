package ekol.crm.inbound.client;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import ekol.crm.inbound.domain.Attachment;
import ekol.model.StringIdNamePair;

@Component
public class FileServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceClient.class);

	@Value("${oneorder.file-service}")
    private String fileServiceName;

	@Autowired
    private RestTemplate restTamplate;

	public Attachment upload(Attachment attachment) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		body.add("file", new ByteArrayResource(attachment.getContent()){
			@Override
			public String getFilename() {
				return attachment.getName();
			}
		});

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		StringIdNamePair response = restTamplate.postForObject(fileServiceName + "/upload", requestEntity, StringIdNamePair.class);
		return new Attachment(response.getId(), response.getName(), null); 
	}


}