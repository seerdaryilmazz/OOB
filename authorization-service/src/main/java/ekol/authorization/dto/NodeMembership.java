package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.authorization.domain.auth.MemberOfRelation;
import ekol.model.IdNamePair;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class NodeMembership  {
	private IdNamePair name;
	private Membership level;
	
	public static NodeMembership with(MemberOfRelation relation) {
		NodeMembership instance = new NodeMembership();
		instance.setName(new IdNamePair(relation.getEntity().getExternalId(), relation.getEntity().getName()));
		instance.setLevel(Membership.fromLevel(relation.getLevel()));
		return instance;
	}
}
