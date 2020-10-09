package ekol.outlook.model.dto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.exceptions.ValidationException;
import ekol.outlook.model.OutlookMail;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class MailJson {

    private String subject;
    private BodyJson body;
    private List<UserJson> internalRecipients;
    private List<UserJson> externalRecipients;
    private List<DocumentJson> attachments;
    private UserJson sender;
    private boolean sendNoteToSender;

}
