package ekol.authorization.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.authorization.domain.auth.AuthTeam;
import ekol.hibernate5.domain.entity.LookupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Teams")
@Where(clause = "deleted = 0")
@Data
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team extends LookupEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7955042183509227777L;
	@Id
    @SequenceGenerator(name = "seq_teams", sequenceName = "seq_teams")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_teams")
    private Long id;
	
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
	private EntityStatus status;
    
    public static Team with(AuthTeam authTeam) {
    	Team team = new Team();
    	team.setCode(StringUtils.substring(authTeam.getName(), 0, 10));
    	team.setName(authTeam.getName());
    	if(Objects.isNull(authTeam.getExternalId())) {
    		team.setStatus(EntityStatus.ACTIVE);
    	}
    	team.setId(authTeam.getExternalId());
    	return team;
    }

}
