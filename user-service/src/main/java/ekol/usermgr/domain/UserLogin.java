package ekol.usermgr.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.Data;

@Entity
@Table(name="USERS_LOGIN")
@SequenceGenerator(name = "SEQ_USERS_LOGIN", sequenceName = "SEQ_USERS_LOGIN")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserLogin {

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USERS_LOGIN")
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;
    
    @Column(nullable = false, length = 100)
    private String clientId;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "loginTime"))
    })
    private UtcDateTime loginTime;
}
