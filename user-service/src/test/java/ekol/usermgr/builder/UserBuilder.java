package ekol.usermgr.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.usermgr.domain.User;
import ekol.usermgr.domain.UserStatus;

public final class UserBuilder {

    private Long id;
    private String username;
    private String displayName;
    private String email;
    private UserStatus status;
    private boolean deleted;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private String office;
    private String sapNumber;
    private String phoneNumber;
    private String mobileNumber;
    private String thumbnailPath;

    private UserBuilder() {
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    public UserBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public UserBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public UserBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }
    public UserBuilder withOffice(String office) {
        this.office = office;
        return this;
    }
    public UserBuilder withSapNumber(String sapNumber) {
        this.sapNumber = sapNumber;
        return this;
    }
    public UserBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public UserBuilder withMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }
    public UserBuilder withThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setStatus(status);
        user.setOffice(office);
        user.setSapNumber(sapNumber);
        user.setPhoneNumber(phoneNumber);
        user.setMobileNumber(mobileNumber);
        user.setDeleted(deleted);
        user.setLastUpdated(lastUpdated);
        user.setLastUpdatedBy(lastUpdatedBy);
        user.setThumbnailPath(thumbnailPath);
        return user;
    }
}
