package ekol.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import ekol.notification.domain.Concern;
import freemarker.template.*;

@Component
public class FreemarkerComponent {
	
	@Autowired
	private Configuration freemarkerConfiguration;
	
	public String process(Concern concern, Object model) {
		try {
			Template template = freemarkerConfiguration.getTemplate(concern.name());
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
