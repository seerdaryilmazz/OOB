package ekol.agreement.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static <T> T getForObject(boolean ignoreNotFoundException, RestTemplate restTemplate, String url, Class<T> responseType, Object... uriVariables) {

        T result = null;

        try {
            result = restTemplate.getForObject(url, responseType, uriVariables);
        } catch(HttpClientErrorException e) {
            if (!ignoreNotFoundException || HttpStatus.NOT_FOUND != e.getStatusCode()) {
                throw e;
            }
        }
        return result;
    }

    public static LocalDate deserializeLocalDateStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return null;
        }
    }
}
