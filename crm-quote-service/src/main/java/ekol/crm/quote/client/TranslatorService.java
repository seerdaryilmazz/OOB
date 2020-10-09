package ekol.crm.quote.client;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.SupportedLocale;
import ekol.exceptions.ResourceNotFoundException;

@Service
public class TranslatorService {

	@Value("${oneorder.translator-service}")
    private String translatorServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public List<SupportedLocale> findAllLocales() {
    	return Optional.ofNullable(restTemplate.getForObject(translatorServiceName + "/supportedLocale", SupportedLocale[].class)).map(Arrays::asList).orElseGet(ArrayList::new);
    }

    public SupportedLocale findLocaleOrThrowResourceNotFoundException(Long id) {
    	return findAllLocales().parallelStream().filter(t->Objects.equals(id, t.getId())).findFirst().orElseThrow(()->new ResourceNotFoundException("Locale not found. id: {0}", id));
    }

    public String translate(String appName, String key, String locale) {
        return restTemplate.getForObject(translatorServiceName + "/translate?appName={appName}&key={key}&locale={locale}", String.class, appName, key, locale);
    }
}
