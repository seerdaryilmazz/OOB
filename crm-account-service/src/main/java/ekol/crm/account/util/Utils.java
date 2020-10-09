package ekol.crm.account.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utils {

    // TODO: LocalDateDeserializer'ın içinde public static bir deserialize metodu olmalı ve
    // manual deserialize işlemlerinde bu metod kullanılmalı.
    public static LocalDate deserializeLocalDateStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return null;
        }
    }

    public static LocalDate getLocalDateAtTimeZone(String timeZoneId) {
        return Instant.now().atZone(ZoneId.of(timeZoneId)).toLocalDate();
    }

    public static <T> T getForObject(boolean ignoreNotFoundException, RestTemplate restTemplate, String url, Class<T> responseType, Object... uriVariables) {

        T result = null;

        try {
            result = restTemplate.getForObject(url, responseType, uriVariables);
        } catch (Exception e) {

            boolean throwException = true;

            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
                if (httpClientErrorException.getStatusCode().equals(HttpStatus.NOT_FOUND) && ignoreNotFoundException) {
                    throwException = false;
                }
            }

            if (throwException) {
                throw e;
            }
        }

        return result;
    }
}
