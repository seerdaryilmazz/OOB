package ekol.usermgr.domain;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.BaseEntity;

@Entity
@Table(name = "Language")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Language extends BaseEntity {

	@Id
	@SequenceGenerator(name = "seq_language", sequenceName = "seq_language")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_language")
	private Long id;

	private String name;

	/**
	 * ISO 639-1 language code
	 */
	private String isoCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

}
