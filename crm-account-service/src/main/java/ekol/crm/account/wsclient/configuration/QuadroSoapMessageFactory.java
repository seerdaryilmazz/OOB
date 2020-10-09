package ekol.crm.account.wsclient.configuration;

import java.io.*;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.ws.soap.saaj.*;

public class QuadroSoapMessageFactory extends SaajSoapMessageFactory {
	
	@Override
	public SaajSoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
		String text = IOUtils.toString(inputStream, Charset.forName("ISO-8859-9"));
		return super.createWebServiceMessage(new ByteArrayInputStream(text.getBytes()));
	}
}