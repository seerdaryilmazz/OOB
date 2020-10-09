package ekol.crm.quote.domain.dto.businessVolume;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class BusinessVolumeJson {
	
	private Long id;
	private String inbound;
	private String storage;
	private String outbound;
	private String vas;
	private Boolean spot;
	
	public BusinessVolume toEntity() {
		BusinessVolume entity = new BusinessVolume();
		entity.setId(getId());
		entity.setInbound(getInbound());
		entity.setStorage(getStorage());
		entity.setOutbound(getOutbound());
		entity.setVas(getVas());
		entity.setSpot(getSpot());
		return entity;
	}
	public static BusinessVolumeJson fromEntity(BusinessVolume entity) {
		if(Objects.isNull(entity)) {
			return null;
		}
		BusinessVolumeJson json = new BusinessVolumeJson();
		json.setId(entity.getId());
		json.setInbound(entity.getInbound());
		json.setStorage(entity.getStorage());
		json.setOutbound(entity.getOutbound());
		json.setVas(entity.getVas());
		json.setSpot(entity.getSpot());
		return json;
	}

}
