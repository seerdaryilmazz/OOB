package ekol.outlook.util.gmail;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import ekol.outlook.model.dto.MailJson;
import ekol.outlook.service.MimeMessageBuilder;

public class GmailService {
	private static final String APPLICATION_NAME = "YOUR_APP_NAME";

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private final HttpTransport httpTransport;
	private GmailCredentials gmailCredentials;

	public GmailService(HttpTransport httpTransport) {
		this.httpTransport = httpTransport;
	}

	public void setGmailCredentials(GmailCredentials gmailCredentials) {
		this.gmailCredentials = gmailCredentials;
	}

	public boolean sendMessage(MailJson mail) throws MessagingException, IOException {
		Message message = createMessageWithEmail(MimeMessageBuilder.build(mail));
		return createGmail().users()
				.messages()
				.send(gmailCredentials.getUserEmail(), message)
				.execute()
				.getLabelIds().contains("SENT");
	}
	

	private Gmail createGmail() {
		Credential credential = authorize();
		return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.setRootUrl(gmailCredentials.getHost())
				.build();
	}

	private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);

		return new Message()
				.setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
	}

	private Credential authorize() {
		return new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(gmailCredentials.getClientId(), gmailCredentials.getClientSecret())
				.build()
				.setAccessToken(gmailCredentials.getAccessToken())
				.setRefreshToken(gmailCredentials.getRefreshToken());
	}}
