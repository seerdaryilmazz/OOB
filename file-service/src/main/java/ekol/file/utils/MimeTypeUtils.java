package ekol.file.utils;

import java.io.*;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.*;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.file.domain.ContentTypeJson;

@Component
public class MimeTypeUtils {
	private Map<String,String> contentTypes;

	@PostConstruct
	private void init() throws IOException {

        try(InputStreamReader inputStreamReader = new InputStreamReader(new ClassPathResource("content-types.json").getInputStream())) {
            ContentTypeJson[] types = new ObjectMapper().readValue(inputStreamReader, ContentTypeJson[].class);
            contentTypes = Stream.of(types).collect(Collectors.toMap(ContentTypeJson::getExtension, ContentTypeJson::getType, (x, y)->x));
        }
	}
	
	public String guessContentType(String filename) {
		String contentType = URLConnection.guessContentTypeFromName(filename);
		if (StringUtils.isBlank(contentType)) {
			return Optional.ofNullable(FilenameUtils.getExtension(filename))
					.map(extention -> StringUtils.prependIfMissing(extention, "."))
					.map(contentTypes::get)
					.orElse("application/octet-stream");
		}
		return contentType;
	}
}
