package ekol.notification.service.freemarker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreemarkerConfig extends FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration {
	
	@Autowired
	private EmailNotificationTemplateLoader emailNotificationTemplateLoader;
	
	@Override
    public FreeMarkerConfigurer freeMarkerConfigurer() {

        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        factory.setDefaultEncoding("UTF-8");
        factory.setPreTemplateLoaders(emailNotificationTemplateLoader); // Bu metoda birden çok loader verilebiliyor.

        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();

        try {
            configurer.setConfiguration(factory.createConfiguration());
        } catch (Exception e) {
            throw new RuntimeException("Exception while creating FreeMarker configuration");
        }

        return configurer;
    }

}
