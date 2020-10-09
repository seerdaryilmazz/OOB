package ekol.authorization.dto;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import ekol.authorization.domain.auth.BaseRelation;
import lombok.Data;

@Data
public class Link {
	private Node source;
	private Node target;
	
	private String type;
	
	public static Link with(BaseRelation relation) {
		return with(Node.with(relation.getStartNode()), Node.with(relation.getEndNode()), relation.getType());
	}
	
	public static Link with(Node source, Node target, String type) {
		Link link = new Link();
		link.setSource(source);
		link.setTarget(target);
		link.setType(type);
		return link;
	}
	
	public int hashCode() {
    	return new HashCodeBuilder()
    			.append(source)
    			.append(target)
    			.append(type)
    			.toHashCode();
    }
    
    public boolean equals(Object object) {

    	if (!(object instanceof Link))
            return false;
        if (object == this)
            return true;

        Link link = (Link) object;
        return new EqualsBuilder().
                append(getSource(), link.getSource()).
                append(getTarget(), link.getTarget()).
                append(getType(), link.getType()).
                isEquals();
    }
}
