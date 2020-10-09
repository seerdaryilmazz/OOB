package ekol.agreement.queue.wscbfunitprice.config;

import java.io.IOException;

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
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import ekol.agreement.queue.wscbfunitprice.client.RainbowClient;

@Configuration
public class WscBfUnitPriceConfiguration {
	
	
	@Value("${rainbow.agreement.endpoint}")
	private String endpoint;
	
	@Value("${rainbow.agreement.username}")
	private String username;

	@Value("${rainbow.agreement.password}")
	private String password;
	
	@Value("${rainbow.agreement.connect-timeout:5000}")
	private Integer connectTimeout;

	@Value("${rainbow.agreement.read-timeout:5000}")
	private Integer readTimeout;

	@Value("${rainbow.agreement.enable:true}")
	private Boolean enable;

	@Value("${oneorder.gateway}")
	private String gatewayUrl;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("ekol.agreement.queue.wscbfunitprice.wsdl");
		return marshaller;
	}

	@Bean
	public RainbowClient rainbowClient(Jaxb2Marshaller marshaller, WebServiceMessageSender webServiceMessageSender) {
		RainbowClient client = new RainbowClient();
		client.setGatewayUrl(gatewayUrl);
		client.setDefaultUri(endpoint);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setMessageSender(webServiceMessageSender);
		client.setEnable(enable);
		client.getWebServiceTemplate().setCheckConnectionForFault(false);
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
}
