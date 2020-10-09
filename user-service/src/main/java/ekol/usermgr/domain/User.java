package ekol.usermgr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.usermgr.serializer.PasswordSerializer;

/**
 * Created by kilimci on 13/04/16.
 */
@Entity
@Table(name = "Users")
@SequenceGenerator(name = "SEQ_USERS", sequenceName = "SEQ_USERS")
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = "deleted = 0")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USERS")
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Nationalized
    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String normalizedName;

    @Column(nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    /**
     * region-based zone id from IANA Time Zone Database...
     * It will be used to create a java.time.ZoneId via ZoneId.of(timeZoneId).
     */
    @Column
    private String timeZoneId;

    @Enumerated(value = EnumType.STRING)
    private UserAuthenticationType authenticationType;

    @Column(length = 20)
    private String sapNumber;

    @Column(length = 100)
    private String office;

    @Column(length = 100)
    private String phoneNumber;

    @Column(length = 100)
    private String mobileNumber;

    @Column
    private String thumbnailPath;

    @Transient
    private String thumbnail;

    @Column
    @JsonSerialize(using = PasswordSerializer.class)
    private String password;

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof User)){
            return false;
        }
        return ((User)other).getId().equals(getId());
    }

    @Override
    public int hashCode(){
        int hashCode = 1;
        hashCode = 31 * hashCode + (getId() == null ? 0 : getId().hashCode());
        hashCode = 31 * hashCode + (getUsername() == null ? 0 : getUsername().hashCode());
        hashCode = 31 * hashCode + (getDisplayName() == null ? 0 : getDisplayName().hashCode());
        hashCode = 31 * hashCode + (getEmail() == null ? 0 : getEmail().hashCode());
        hashCode = 31 * hashCode + (getStatus() == null ? 0 : getStatus().hashCode());
        return hashCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(UserAuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSapNumber() {
        return sapNumber;
    }

    public void setSapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
}
