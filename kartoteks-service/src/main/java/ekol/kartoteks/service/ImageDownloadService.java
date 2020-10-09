package ekol.kartoteks.service;

import ekol.exceptions.ApplicationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by kilimci on 08/12/2017.
 */
@Service
public class ImageDownloadService {

    @Value("${oneorder.companyImages.directory}")
    private String imageDirectory;

    private Logger logger = LoggerFactory.getLogger(ImageDownloadService.class);

    private String buildFilePath(String fileName){
        if(StringUtils.isBlank(imageDirectory)){
            throw new IllegalArgumentException("oneorder.companyImages.directory is null");
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(imageDirectory);
        if(!imageDirectory.endsWith("/")){
            sb.append("/");
        }
        sb.append(fileName);
        return sb.toString();
    }
    private String getMimeType(HttpResponse response){
        Header contentType = response.getEntity().getContentType();
        if(contentType != null && StringUtils.isNotBlank(contentType.getValue())){
            return contentType.getValue().split(";")[0].trim();
        }
        return null;
    }
    private String getFileName(String extension){
        return UUID.randomUUID().toString() + "." + extension;
    }
    public String downloadAndSave(String imageUrl) {
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpget = new HttpGet(imageUrl);
        String fileName;
        try {
            HttpResponse response = httpClient.execute(httpget);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                logger.warn("image download for '{}' returned status code: {}", imageUrl, response.getStatusLine().getStatusCode());
                return null;
            }
            String mimeType = getMimeType(response);
            if(StringUtils.isBlank(mimeType) || !mimeType.startsWith("image/")){
                logger.warn("image download for '{}' returned invalid mime type: {}", imageUrl, mimeType);
                return null;
            }
            String extension = mimeType.substring(mimeType.indexOf("/") + 1);
            fileName = getFileName(extension);
            String filePath = buildFilePath(fileName);
            FileUtils.writeByteArrayToFile(new File(filePath), EntityUtils.toByteArray(response.getEntity()));
        } catch (IOException e) {
            throw new ApplicationException("Error downloading image: " + imageUrl, e);
        } finally{
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("error closing http client", e);
            }
        }
        return fileName;

    }
}
