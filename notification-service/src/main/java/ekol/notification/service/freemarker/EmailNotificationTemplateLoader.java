package ekol.notification.service.freemarker;

import java.io.*;
import java.time.ZoneId;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.notification.domain.*;
import ekol.notification.service.NotificationTemplateService;
import freemarker.cache.TemplateLoader;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailNotificationTemplateLoader implements TemplateLoader {
	
	@Autowired
	private NotificationTemplateService notificationTemplateService;

	@Override
	public Object findTemplateSource(String name) throws IOException {
		Concern concern = Arrays.stream(Concern.values()).parallel().filter(t->name.startsWith(t.name())).findFirst().orElse(null);
		return notificationTemplateService.findActiveTemplate(concern, Channel.EMAIL);
	}

	@Override
	public long getLastModified(Object templateSource) {
		return NotificationTemplate.class.cast(templateSource).getLastUpdated().atZone(ZoneId.systemDefault()).toEpochSecond();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader(NotificationTemplate.class.cast(templateSource).getFreemarkerContent().getFileContent());
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
	}

}
