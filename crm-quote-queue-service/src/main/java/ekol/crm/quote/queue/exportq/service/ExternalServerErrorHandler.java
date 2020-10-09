package ekol.crm.quote.queue.exportq.service;

import ekol.exceptions.ApplicationException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class ExternalServerErrorHandler implements ResponseErrorHandler  {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String text = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name());
        String message = MessageFormat.format("External server error: {0} message: {1}", response.getRawStatusCode(), text);
        throw new ApplicationException(message);
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus.Series series = response.getStatusCode().series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }

}
