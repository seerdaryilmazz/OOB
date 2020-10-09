package ekol.agreement.queue.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class Utils {

    public static <T> T getForObject(boolean ignoreNotFoundException, RestTemplate restTemplate, String url, Class<T> responseType) {

        T result = null;

        try {
            result = restTemplate.getForObject(url, responseType);
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
    public static String stringifyXml(Object xml){
        JAXBContext jaxbContext;
        String xmlString = null;
        try {
            jaxbContext = JAXBContext.newInstance(xml.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(xml, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
