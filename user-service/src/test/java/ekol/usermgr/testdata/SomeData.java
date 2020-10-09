package ekol.usermgr.testdata;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.usermgr.builder.DateTimeWindowBuilder;
import ekol.usermgr.builder.UserBuilder;
import ekol.usermgr.domain.UserStatus;

public class SomeData {

    public static UserBuilder someUser() {
        return UserBuilder.aUser()
                .withUsername(RandomStringUtils.randomAlphabetic(5))
                .withDisplayName(RandomStringUtils.randomAlphabetic(5))
                .withEmail(RandomStringUtils.randomAlphabetic(20))
                .withOffice(RandomStringUtils.randomAlphabetic(12))
                .withSapNumber(RandomStringUtils.randomNumeric(8))
                .withPhoneNumber(RandomStringUtils.randomNumeric(12))
                .withMobileNumber(RandomStringUtils.randomNumeric(12))
                .withThumbnailPath(RandomStringUtils.randomAlphabetic(100))
                .withStatus(UserStatus.ACTIVE)
                .withDeleted(false)
                .withLastUpdated(new UtcDateTime())
                .withLastUpdatedBy("test-user");
    }

    public static DateTimeWindowBuilder someDateTimeWindow() {
        return DateTimeWindowBuilder.aDateTimeWindow()
                .withStart(LocalDateTime.now().minusDays(2L))
                .withEnd(LocalDateTime.now().plusDays(2L));
    }

}