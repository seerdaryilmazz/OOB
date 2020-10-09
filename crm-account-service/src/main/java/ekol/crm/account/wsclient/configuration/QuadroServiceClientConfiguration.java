package ekol.crm.account.wsclient.configuration;

import java.io.IOException;
import java.util.*;

import javax.xml.soap.SOAPMessage;

import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import ekol.crm.account.wsclient.*;

@Configuration
public class QuadroServiceClientConfiguration {
	
	
	@Value("${quadro.company-blockage.endpoint}")
	private String companyBlockageEndpoint;
	
	@Value("${quadro.company-cr-info.endpoint}")
    private String companyCrInfoEndpoint;
	
	@Value("${quadro.username}")
	private String username;

	@Value("${quadro.password}")
	private String password;
	
	@Value("${quadro.company-blockage.connect-timeout:5000}")
	private Integer connectTimeout;

	@Value("${quadro.company-blockage.read-timeout:5000}")
	private Integer readTimeout;

	@Value("${quadro.company-blockage.enable:true}")
	private Boolean enable;

	@Bean (name = "messageFactory")
    public QuadroSoapMessageFactory messageFactory () {
        Map<String, Object> props = new HashMap<>();
        props.put(SOAPMessage.WRITE_XML_DECLARATION, "true");
        props.put(SOAPMessage.CHARACTER_SET_ENCODING, "iso-8859-1");

        QuadroSoapMessageFactory msgFactory = new QuadroSoapMessageFactory();
        msgFactory.setMessageProperties(props);
        msgFactory.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);

        return msgFactory;
    }

	@Bean
    public CompanyCrInfoClient companyCrInfoClient(WebServiceMessageSender webServiceMessageSender, WebServiceMessageFactory messageFactory) {
        CompanyCrInfoClient client = new CompanyCrInfoClient();
        client.setDefaultUri(companyCrInfoEndpoint);
        client.setMarshaller(marshaller("ekol.crm.account.wsclient.companycrinfo.wsdl"));
        client.setUnmarshaller(marshaller("ekol.crm.account.wsclient.companycrinfo.wsdl"));
        client.setMessageSender(webServiceMessageSender);
        client.setEnable(enable);
        client.setMessageFactory(messageFactory);
        return client;
    }

	@Bean
	public CompanyBlockageClient companyBlockageClient(WebServiceMessageSender webServiceMessageSender, WebServiceMessageFactory messageFactory) {
		CompanyBlockageClient client = new CompanyBlockageClient();
		client.setDefaultUri(companyBlockageEndpoint);
		client.setMarshaller(marshaller("ekol.crm.account.wsclient.companyblockage.wsdl"));
		client.setUnmarshaller(marshaller("ekol.crm.account.wsclient.companyblockage.wsdl"));
		client.setMessageSender(webServiceMessageSender);
		client.setEnable(enable);
		client.setMessageFactory(messageFactory);
		return client;
	}

	@Bean
	public WebServiceMessageSender webServiceMessageSender(CredentialsProvider credentialsProvider) {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(readTimeout)
				.build();
		
		HttpClient httpClient = HttpClientBuilder.create()
				.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
				.addInterceptorFirst(new PreemptiveAuthInterceptor(credentialsProvider))
				.setConnectionManager(new PoolingHttpClientConnectionManager())
				.setDefaultRequestConfig(requestConfig)
				.build();
		
		HttpComponentsMessageSender sender =  new HttpComponentsMessageSender(httpClient);
		sender.setCredentials(new UsernamePasswordCredentials(username, password));
		return sender;
	}
	
	@Bean
	public CredentialsProvider credentialsProvider() {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		return credentialsProvider;
	}
	
	private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
		
		private CredentialsProvider credentialsProvider;
		
		public PreemptiveAuthInterceptor(CredentialsProvider credentialsProvider) {
			this.credentialsProvider = credentialsProvider;
		}

	    public void process(final HttpRequest request, final HttpContext context)  throws HttpException, IOException {

	        AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

	        // If no auth scheme is avaialble yet, initialize it preemptively
	        if ( authState.getAuthScheme() == null ) {

	            HttpHost targetHost = (HttpHost) context.getAttribute( HttpCoreContext.HTTP_TARGET_HOST);

	            Credentials creds = credentialsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));

	            if ( creds == null ) {
	                throw new HttpException("no credentials available for preemptive " + "authentication");
	            }

	            authState.update(new BasicScheme(), creds);
	        }
	    }
	}
	
	private Jaxb2Marshaller marshaller(String contextPath) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath(contextPath);
		return marshaller;
	}
}
