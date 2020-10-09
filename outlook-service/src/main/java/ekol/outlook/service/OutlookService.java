package ekol.outlook.service;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

import ekol.exceptions.*;
import ekol.outlook.model.*;
import ekol.outlook.model.dto.MailJson;
import ekol.outlook.repository.*;
import ekol.outlook.util.RequestResponseLoggingInterceptor;
import ekol.outlook.util.gmail.*;

@Service
public class OutlookService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OutlookService.class);
	
	@Value("${google.api-url}")
	private String googleApiUrl;
	
    @Value("${outlook.authorizeUrl}")
    private String authorizeUrl;
    
    @Value("${outlook.applicationId}")
    private String appId;
    
    @Value("${outlook.secret}")
    private String appPassword;
    
    @Value("${outlook.redirectUrl}")
    private String redirectUrl;
    
    @Autowired
    @Qualifier("outlook-client")
    private RestTemplate restTemplate;
    
    private static String scopes = String.join(" ",Arrays.asList(
    		"openid",
            "email",
            "profile",
            "https://www.googleapis.com/auth/gmail.send",
            "https://www.googleapis.com/auth/gmail.compose",
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/calendar.events")); 

    private AccountRepository accountRepository;
    private EventRepository eventRepository;
    
    @Autowired
    public OutlookService(AccountRepository accountRepository,
                          EventRepository eventRepository){
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;

    }

    public String getLoginUrl(UUID state, UUID nonce) {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
        urlBuilder.queryParam("client_id", appId);
        urlBuilder.queryParam("redirect_uri", redirectUrl);
        urlBuilder.queryParam("response_type", "code id_token");
        urlBuilder.queryParam("access_type", "offline");
        urlBuilder.queryParam("scope", scopes);
        urlBuilder.queryParam("state", state);
        urlBuilder.queryParam("nonce", nonce);
        urlBuilder.queryParam("response_mode", "form_post");
        return urlBuilder.toUriString();
    }

    public TokenHolder getTokens(String grantType, String code) {
        String url = new StringBuilder(googleApiUrl).append("/oauth2/v4/token").toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", appId);
        map.add("client_secret", appPassword);
        map.add("grant_type", grantType);
        map.add("authorization_code".equalsIgnoreCase(grantType) ? "code" : grantType, code);
        if("authorization_code".equalsIgnoreCase(grantType)) {
        	map.add("redirect_uri", redirectUrl);
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        ResponseEntity<TokenHolder> response = restTemplate.exchange(UriComponentsBuilder.fromUriString(url).build().toUri() ,HttpMethod.POST, request, TokenHolder.class);

        if(!response.getStatusCode().is2xxSuccessful()){
        	throw new BadRequestException(response.getBody().getErrorDescription());
        }
        TokenHolder tokenHolder = response.getBody();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, tokenHolder.getExpiresIn());
        tokenHolder.setExpirationTime(now.getTime());

        return tokenHolder;
    }
    
    public Account refreshToken(Account account) {
    	if(!account.getTokenHolder().hasRefreshToken()) {
    		String message = MessageFormat.format("{0} has no refresh token", account.getUsername());
    		LOGGER.warn(message);
    		throw new BadRequestException(message);
    	}
    	
    	try {
    		String refreshToken = account.getTokenHolder().getRefreshToken();
    		TokenHolder tokenHolder = getTokens("refresh_token", refreshToken);
    		account.getTokenHolder().setAccessToken(tokenHolder.getAccessToken());
    		account.getTokenHolder().setExpirationTime(tokenHolder.getExpirationTime());
    		account.getTokenHolder().setExpiresIn(tokenHolder.getExpiresIn());
    		LOGGER.warn("{} access token is refreshed", account.getUsername());
    		return accountRepository.save(account);
    	} catch(Exception e) {
    		LOGGER.warn("{} access token is error", account.getUsername());
    		throw e;
    	}
    }
    

    @Transactional
    public void createOrUpdateEvent(String sender, OutlookEvent event){
        Account account = getAccountByMailAddress(sender);
        if(!account.getTokenHolder().isValidToken()) {
        	LOGGER.warn("{} can not create/update calendar event. token is expired", account.getUsername());
        	account = refreshToken(account);
        }
        
        OutlookEvent existingEvent = eventRepository.findBySourceId(event.getSourceId());
        boolean cancel = Optional.of(event).map(OutlookEvent::getCancel).orElse(Boolean.FALSE).booleanValue();
        if(existingEvent != null){
        	if(cancel) {
        		cancelEvent(account, existingEvent.getId());
        	} else {
        		OutlookEvent diffEvent = findDifference(existingEvent, event);
        		if(diffEvent == null){
        			return;
        		}
        		updateEvent(account, diffEvent, existingEvent.getId());
        	}
        }else if(!cancel){
            event.setId(createEvent(account, event));
        }
        eventRepository.save(event);
    }

    public void sendMail(String sender, MailJson mail){
    	try {
        	Account account = getAccountByMailAddress(sender);
        	if(!account.getTokenHolder().isValidToken()) {
            	LOGGER.warn("{} can not send email. Token is expired", account.getUsername());
            	account = refreshToken(account);
            }
        	
    		GmailService gmailService = new GmailService(GoogleNetHttpTransport.newTrustedTransport());
    		gmailService.setGmailCredentials(GmailCredentials.builder()
                    .userEmail(sender)
                    .clientId(appId)
                    .clientSecret(appPassword)
                    .host(googleApiUrl)
                    .accessToken(account.getTokenHolder().getAccessToken())
                    .refreshToken(account.getTokenHolder().getRefreshToken())
                    .build());
    		gmailService.sendMessage(mail);
    	} catch(Exception e) {
    		LOGGER.error(MessageFormat.format("{0}, Mail send error.", sender), e);
    	}
    }

    private String createEvent(Account account, OutlookEvent event){
        String url = googleApiUrl + "/calendar/v3/calendars/primary/events?sendUpdates=all";
        HttpEntity<OutlookEvent> request = new HttpEntity<>(event, getHttpHeaders(account.getTokenHolder().getAccessToken()));
        
        return restTemplate.postForObject(url, request, OutlookEvent.class).getId();
    }

    private void updateEvent(Account account, OutlookEvent event, String eventId){
    	String url = googleApiUrl + "/calendar/v3/calendars/primary/events/{eventId}?sendUpdates=all";
       
    	Map<String, String> uriParams = new HashMap<>();
        uriParams.put("eventId", eventId);
        
        HttpEntity<OutlookEvent> request = new HttpEntity<>(event, getHttpHeaders(account.getTokenHolder().getAccessToken()));
        restTemplate.exchange(UriComponentsBuilder.fromUriString(url).buildAndExpand(uriParams).toUri() ,HttpMethod.PATCH, request, Void.class);
    }
    
    private void cancelEvent(Account account, String eventId){
    	String url = googleApiUrl + "/calendar/v3/calendars/primary/events/{eventId}?sendUpdates=all";

        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("eventId", eventId);

        HttpEntity<OutlookEvent> request = new HttpEntity<>(getHttpHeaders(account.getTokenHolder().getAccessToken()));
        restTemplate.exchange(UriComponentsBuilder.fromUriString(url).buildAndExpand(uriParams).toUri() ,HttpMethod.DELETE, request, Void.class);
    }

    public OutlookEvent findDifference(OutlookEvent existingEvent, OutlookEvent event){
    	OutlookEvent eventDiff = new OutlookEvent();
    	boolean diffExits = false;
    	if(!StringUtils.equals(existingEvent.getDescription(), event.getDescription())){
    		diffExits = true;
    		eventDiff.setDescription(event.getDescription());
    	}
    	if(!StringUtils.equals(existingEvent.getSummary(), event.getSummary())){
    		diffExits = true;
    		eventDiff.setSummary(event.getSummary());
    	}
    	if(!StringUtils.equals(existingEvent.getVisibility(), event.getVisibility())){
    		diffExits = true;
    		eventDiff.setVisibility(event.getVisibility());
    	}
    	if(!Objects.equals(existingEvent.getLocation(), event.getLocation())){
    		diffExits = true;
    		eventDiff.setLocation(event.getLocation());
    	}
    	if(!listEqualsIgnoreOrder(existingEvent.getAttendees(), event.getAttendees())){
    		diffExits = true;
    		eventDiff.setAttendees(event.getAttendees());
    	}
    	if(!Objects.equals(existingEvent.getStart(), event.getStart())){
    		diffExits = true;
    		eventDiff.setStart(event.getStart());
    	}
    	if(!Objects.equals(existingEvent.getEnd(), event.getEnd())){
    		diffExits = true;
    		eventDiff.setEnd(event.getEnd());
    	}
    	return diffExits ? eventDiff : null;
    }

    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return list1 == null ? list2 == null : new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    @Transactional
    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }

    public Account getAccountByMailAddress(String mailAddress) {
        return accountRepository.findByMailAddress(mailAddress).orElseThrow(() -> new ResourceNotFoundException("Account with mail Address {0} not found", mailAddress));
    }
    
    private static HttpHeaders getHttpHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "one-order");
        headers.add("client-request-id", UUID.randomUUID().toString());
        headers.add("return-client-request-id", "true");
        headers.add("Authorization", String.format("Bearer %s", accessToken));
        return headers;
    }
    
    @Bean
    @Qualifier("outlook-client")
    public RestTemplate outlookClient() {
    	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    	BufferingClientHttpRequestFactory buffer = new BufferingClientHttpRequestFactory(factory);
        RestTemplate outlookClient = new RestTemplate(buffer);
        outlookClient.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return outlookClient;
    }
}