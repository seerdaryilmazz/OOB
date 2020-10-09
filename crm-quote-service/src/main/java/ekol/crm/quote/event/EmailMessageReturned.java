package ekol.crm.quote.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.EmailMessage;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessageReturned {

    private String from;
    private String replyTo;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private Boolean html;
    private List<EmailMessage.Attachment> attachments;

    public EmailMessageReturned() {
    }

    public EmailMessageReturned(String from, String replyTo, List<String> to, List<String> cc, List<String> bcc, String subject, String body, Boolean html, List<EmailMessage.Attachment> attachments) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.body = body;
        this.html = html;
        this.attachments = attachments;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }

    public List<EmailMessage.Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EmailMessage.Attachment> attachments) {
        this.attachments = attachments;
    }
}
