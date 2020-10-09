package ekol.outlook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OutlookMail {


    @JsonProperty("Message")
    private Message message;

    @JsonProperty("SaveToSentItems")
    private boolean saveToSentItems;

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        @JsonProperty("Subject")
        private String subject;
        @JsonProperty("Body")
        private Body body;
        @JsonProperty("ToRecipients")
        private List<User> recipients;
        @JsonProperty("Attachments")
        private List<Attachment> attachments;

    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body{
        @JsonProperty("ContentType")
        private String contentType;
        @JsonProperty("Content")
        private String content;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User{
        @JsonProperty("EmailAddress")
        EmailAddress emailAddress;

    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailAddress{
        @JsonProperty("Address")
        private String address;
        @JsonProperty("Name")
        private String name;

    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment{
        @JsonProperty("@odata.type")
        private String type = "#Microsoft.OutlookServices.FileAttachment";
        @JsonProperty("Name")
        private String name;
        @JsonProperty("ContentBytes")
        private byte[] contentBytes;
    }


}
