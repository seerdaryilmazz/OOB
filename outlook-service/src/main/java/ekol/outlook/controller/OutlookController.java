package ekol.outlook.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import ekol.exceptions.*;
import ekol.outlook.client.FileServiceClient;
import ekol.outlook.model.*;
import ekol.outlook.model.dto.MailJson;
import ekol.outlook.service.OutlookService;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/outlook")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OutlookController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OutlookController.class);
	
	private SessionOwner sessionOwner;
    private OutlookService outlookService;
    private FileServiceClient fileService;

    @GetMapping("/loginUrl")
    public String getLoginUrl(HttpServletRequest request) {
        UUID state = UUID.randomUUID();
        UUID nonce = UUID.randomUUID();
        return outlookService.getLoginUrl(state, nonce);
    }

    @GetMapping("/isValid")
    public Boolean checkIfAccountIsValid(@RequestParam String sender) {
        try {
        	Account account = outlookService.getAccountByMailAddress(sender);
        	if(account.getTokenHolder().isValidToken()) {
        		return true;
        	} else {
        		outlookService.refreshToken(account);
        		return true;
        	}
        } catch (Exception e){
            LOGGER.error("Error verifying user outlook account", e);
        }
        return false;
    }

    @ResponseBody
    @PostMapping("/authorize")
    public String authorize(
            @RequestParam("code") String code,
            @RequestParam("id_token") String idToken,
            @RequestParam("state") UUID state,
            HttpServletRequest request) throws Exception {
        Account account = null;
        IdToken idTokenObj = IdToken.parseEncodedToken(idToken);
        if (idTokenObj != null) {
            TokenHolder tokens = outlookService.getTokens("authorization_code", code);
            try{
                account = outlookService.getAccountByMailAddress(idTokenObj.getEmail());
                if(StringUtils.isBlank(tokens.getRefreshToken())) {
                	tokens.setRefreshToken(account.getTokenHolder().getRefreshToken());
                }
                account.setTokenHolder(tokens);
            }catch (ResourceNotFoundException e){
                account = Account.builder()
                        .username(sessionOwner.getCurrentUser().getUsername())
                        .tenantId(idTokenObj.getTenantId())
                        .mailAddress(idTokenObj.getEmail())
                        .tokenHolder(tokens).build();
            }finally {
            	outlookService.saveAccount(account);
			}
            return "<script>window.close();</script>";
        }else{
        	LOGGER.error("Outlook id token could not be parsed");
            throw new ValidationException("Outlook authentication could not be verified");
        }
    }

    @PostMapping("/sendMail")
    public void sendMail(@RequestBody MailJson request) {

        if(!CollectionUtils.isEmpty(request.getAttachments())) {
            request.getAttachments().forEach(document->document.setBytes(fileService.download(document.getDocumentId())));
        }
        outlookService.sendMail(request.getSender().getEmailAddress(), request);
    }
}
