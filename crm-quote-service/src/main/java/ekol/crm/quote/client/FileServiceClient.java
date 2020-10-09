package ekol.crm.quote.client;

import java.io.*;
import java.nio.file.Files;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import ekol.model.StringIdNamePair;

@Service
public class FileServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceClient.class);

	@Value("${oneorder.file-service}")
    private String fileServiceName;

	@Autowired
    private RestTemplate restTamplate;

	public StringIdNamePair upload(File file) {
		StringIdNamePair response = null;
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

			body.add("file", new ByteArrayResource(Files.readAllBytes(file.toPath())){
				@Override
				public String getFilename() {
					return file.getName();
				}
			});

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			response = restTamplate.postForObject(fileServiceName + "/upload", requestEntity, StringIdNamePair.class);
		} catch (IOException e) {
			LOGGER.warn("File Upload fail", e);
		}
		return response;
	}


}
