package ekol.authorization.service.auth;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.auth.BaseRelation;
import ekol.authorization.domain.dto.FromTo;
import ekol.authorization.dto.InheritRelation;
import ekol.authorization.dto.Link;
import ekol.authorization.dto.MemberOfRelation;
import ekol.authorization.dto.Node;
import ekol.authorization.dto.Relation;
import ekol.authorization.repository.auth.RelationRepository;
import ekol.exceptions.BadRequestException;

/**
 * Created by ozer on 08/03/2017.
 */
@Service
public class RelationService {
	
	private static final String INHERIT = "INHERIT";

    @Autowired
    private NodeService nodeService;

    @Autowired
    private RelationRepository relationRepository;

    @Transactional
    public FromTo getFromTo(Relation relation) {
        if (relation == null) {
            throw new BadRequestException("Relation can not be null");
        }

        if (!relation.isValid()) {
            throw new BadRequestException("Relation is not valid");
        }

        return new FromTo(
                nodeService.createOrLoadFromNode(relation.getFrom()),
                nodeService.createOrLoadFromNode(relation.getTo()));
    }
    
    public Optional<BaseRelation> getRelation(Long fromId, Long toId, String relationType) {
    	return relationRepository.getRelations(fromId, toId).parallelStream().filter(t->t.getType().equals(relationType)).findFirst();
    }
    
    @Transactional
    public void deleteInheritRelation(Long fromId, Long toId) {
    	relationRepository.deleteInheritRelation(fromId, toId);
    }

    @Transactional
    public void deleteMemberOfRelation(Long fromId, Long toId) {
    	relationRepository.deleteMemberOfRelation(fromId, toId);
    }

    @Transactional
    public Link mergeInheritRelation(InheritRelation inheritRelation) {
        FromTo fromTo = getFromTo(inheritRelation);
        if(fromTo.getFrom().getId().equals(fromTo.getTo().getId())) {
        	throw new BadRequestException("can not make relation itself");
        }
        if(getRelation(fromTo.getFrom().getId(), fromTo.getTo().getId(), INHERIT).isPresent()) {
        	throw new BadRequestException("This relation is exist");
        }
        relationRepository.mergeInheritRelation(fromTo.getFrom().getId(), fromTo.getTo().getId());
        return Link.with(Node.with(fromTo.getFrom()), Node.with(fromTo.getTo()), INHERIT);
    }

    @Transactional
    public void mergeMemberOfRelation(MemberOfRelation memberOfRelation) {
        FromTo fromTo = getFromTo(memberOfRelation);
        Long startDate = memberOfRelation.getMembershipDateRange() != null && memberOfRelation.getMembershipDateRange().getStartDate() != null ? memberOfRelation.getMembershipDateRange().getStartDate().toEpochDay() : null;
        Long endDate = memberOfRelation.getMembershipDateRange() != null && memberOfRelation.getMembershipDateRange().getEndDate() != null ? memberOfRelation.getMembershipDateRange().getEndDate().toEpochDay() : null;
        relationRepository.mergeMemberOfRelation(fromTo.getFrom().getId(), fromTo.getTo().getId(), memberOfRelation.getLevel(), startDate, endDate);
    }

    @Transactional
    public void mergeMembersOfRelation(List<MemberOfRelation> membersOfRelation) {
        membersOfRelation.forEach(this::mergeMemberOfRelation);
    }
}
