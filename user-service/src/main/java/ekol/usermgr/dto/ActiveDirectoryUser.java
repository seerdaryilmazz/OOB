package ekol.usermgr.dto;

import javax.naming.NamingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by kilimci on 27/12/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActiveDirectoryUser {

    private String username;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private String office;
    private String sapNumber;
    private String thumbnail;

    public static ActiveDirectoryUser createWith(DirContextOperations userOperations){
        ActiveDirectoryUser adUser = new ActiveDirectoryUser();
        String username = readAttrAsString(userOperations, "userPrincipalName").toLowerCase();
        if(username.contains("@")){
            adUser.setUsername(username.split("@")[0]);
        }else{
            adUser.setUsername(username);
        }
        adUser.setDisplayName(readAttrAsString(userOperations, "displayname"));
        adUser.setEmail(readAttrAsString(userOperations, "mail"));
        adUser.setPhoneNumber(readAttrAsString(userOperations, "telephonenumber"));
        adUser.setMobileNumber(readAttrAsString(userOperations, "mobile"));
        adUser.setOffice(readAttrAsString(userOperations, "physicaldeliveryofficename"));
        String rawSapNumber = readAttrAsString(userOperations, "homephone");
        if(rawSapNumber != null){
            StringBuilder sapNumber = new StringBuilder(12);
            for(int i = 0; i < rawSapNumber.length(); i++){
                if(Character.isDigit(rawSapNumber.charAt(i))){
                    sapNumber.append(rawSapNumber.charAt(i));
                }
            }
            adUser.setSapNumber(sapNumber.toString());
        }
        byte[] thumbnail = readAttrAsByteArray(userOperations, "thumbnailphoto");
        if(thumbnail != null){
            adUser.setThumbnail(Base64.encodeBase64String(thumbnail));
        }
        return adUser;
    }

    private static Object readAttr(DirContextOperations userOperations, String name){
        try {
            return userOperations.getAttributes().get(name) != null ? userOperations.getAttributes().get(name).get() : null;
        } catch (NamingException e) {
            Logger logger = LoggerFactory.getLogger(ActiveDirectoryUser.class);
            logger.error("Error parsing LDAP user account", e);
            return null;
        }
    }
    private static byte[] readAttrAsByteArray(DirContextOperations userOperations, String name){
        return (byte[])readAttr(userOperations, name);
    }
    private static String readAttrAsString(DirContextOperations userOperations, String name){
        return (String)readAttr(userOperations, name);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSapNumber() {
        return sapNumber;
    }

    public void setSapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
