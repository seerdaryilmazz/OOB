package ekol.crm.opportunity.util;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by Dogukan Sahinturk on 8.01.2020
 */
public class Utils {

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

    public static <T> boolean isEmptyOrNull(T obj){
        if (obj instanceof Iterable){
            return CollectionUtils.isEmpty((Collection<?>) obj);
        }else return Objects.isNull(obj);
    }
}
