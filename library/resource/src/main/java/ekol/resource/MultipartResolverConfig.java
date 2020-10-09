package ekol.resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.PostConstruct;

/**
 * TODO: Aşağıdaki açıklamaları dikkate alıp durumu tekrar değerlendirmek lazım.
 * Teorik olarak bu ayarı yapmamıza gerek yok çünkü org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration
 * sayesinde bir StandardServletMultipartResolver oluşturuluyor ve applicationContext'e multipartResolver olarak kaydediliyor,
 * böylece dosya upload işlemlerinde sorun yaşamamamız gerekiyor. Durum şu adreste açıklanıyor: https://github.com/spring-projects/spring-boot/issues/2958
 * Ancak dosya upload işlemlerinde sorun yaşıyoruz, "Required request part '...' is not present" şeklinde hata alıyoruz.
 * Çözüm olarak CommonsMultipartResolver'ı kullanıyoruz ve bu ayar Undertow kullandığımız sürece işe yarıyor, yani Tomcat'te çalışmıyor.
 * Multipart ayarlarına örnek olarak şu adres incelenebilir: http://www.baeldung.com/spring-file-upload
 */
@Configuration
public class MultipartResolverConfig {

    @Value("${spring.http.multipart.max-request-size:}")
    private String maxRequestSize;

    @Value("${spring.http.multipart.max-file-size:}")
    private String maxFileSize;

    @PostConstruct
    public void init() {

        MultipartProperties multipartProperties = new MultipartProperties();

        if (StringUtils.isBlank(maxRequestSize)) {
            maxRequestSize = multipartProperties.getMaxRequestSize();
        }

        if (StringUtils.isBlank(maxFileSize)) {
            maxFileSize = multipartProperties.getMaxFileSize();
        }
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(parseSize(maxRequestSize));
        multipartResolver.setMaxUploadSizePerFile(parseSize(maxFileSize));
        return multipartResolver;
    }

    /**
     * Bu metod org.springframework.boot.context.embedded.MultipartConfigFactory.class'tan alınmıştır.
     */
    private long parseSize(String size) {
        Assert.hasLength(size, "Size must not be empty");
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024;
        }
        if (size.endsWith("MB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
        }
        return Long.valueOf(size);
    }
}
