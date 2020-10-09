package ekol.email.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import ekol.email.domain.EmailMessage.Attachment;
import ekol.model.StringIdNamePair;

@Component
public class FileServiceClient {
	
	@Value("${oneorder.file-service}")
	private String fileServiceName;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public StringIdNamePair upload(Attachment attachment) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		body.add("file", new ByteArrayResource(Base64.decodeBase64(attachment.getBase64EncodedContent())){
			@Override
			public String getFilename() {
				return attachment.getName();
			}
		});

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		return restTemplate.postForObject(fileServiceName + "/upload", requestEntity, StringIdNamePair.class);
	}
	
	public Attachment download(String attachmentId) {
		StringIdNamePair attachment = restTemplate.getForObject(fileServiceName + "/{id}", StringIdNamePair.class, attachmentId);

		return restTemplate.execute(fileServiceName + "/{id}/download", HttpMethod.GET, null, clientHttpResponse ->{
			String base64Content = Base64.encodeBase64String(IOUtils.toByteArray(clientHttpResponse.getBody()));
			return new Attachment(attachment.getName(), base64Content);
		}, attachment.getId());
	}
}
