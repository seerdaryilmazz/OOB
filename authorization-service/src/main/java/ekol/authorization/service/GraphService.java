package ekol.authorization.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.auth.*;
import ekol.authorization.dto.*;
import ekol.authorization.repository.auth.BaseRepository;
import ekol.authorization.service.auth.NodeService;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GraphService {
	private BaseRepository baseRepository;
	private NodeService nodeService;

	public BaseEntity findNodeOrThrowException(Long id, int depth) {
		return StreamSupport.stream(nodeService.findByIdWithInheritRelation(id, depth).spliterator(), true).filter(p->p.getId().equals(id)).findFirst().orElseThrow(() -> new ResourceNotFoundException("Node with id {0} not found", String.valueOf(id)));
	}

	public Graph findByExternalId(Long externalId, int depth, String direction) {
		Long id = Optional.ofNullable(nodeService.loadFromExternalId(externalId, AuthTeam.class))
				.map(AuthTeam::getId)
				.orElseThrow(() -> new ResourceNotFoundException("Team with id {0} not found", String.valueOf(externalId)));
		return findById(id, depth, direction);
	}
	
	public Graph findById(Long id, int depth, String direction) {
		BaseEntity node = findNodeOrThrowException(id, depth);
		Graph incomings = StringUtils.isEmpty(direction) || "INCOMING".equals(direction) ? incomings(node, depth) : Graph.with();
		Graph outcomings = StringUtils.isEmpty(direction) || "OUTCOMING".equals(direction) ? outgoings(node, depth) : Graph.with();
		return expandGraphForMissedEntities(incomings.merge(outcomings));
	}

	private Graph incomings(BaseEntity entity, int depth) {
		Graph result = Graph.with();
		Node node = Node.with(entity);
		result.getNodes().add(node);
		Set<Inherit> filtered = entity.getInherits().parallelStream().filter(t->node.equals(Node.with(t.getEndNode()))).collect(Collectors.toSet());
		for (Inherit relation : filtered) {
			result.getLinks().add(Link.with(relation));
			if(depth > 0) {
				result.merge(incomings(relation.getStartNode(), depth -1));
			}
		}
		return result;
	}

	private Graph outgoings(BaseEntity entity, int depth) {
		Graph result = Graph.with();
		Node node = Node.with(entity);
		result.getNodes().add(node);
		Set<Inherit> filtered = entity.getInherits().parallelStream().filter(t->node.equals(Node.with(t.getStartNode()))).collect(Collectors.toSet());
		for (Inherit relation : filtered) {
			result.getLinks().add(Link.with(relation));
			if(depth > 0) {
				result.merge(outgoings(relation.getEndNode(), depth -1));
			}
		}
		return result;
	}

	private Graph expandGraphForMissedEntities(Graph graph) {
		graph.getLinks().iterator().forEachRemaining(link->{
			if(!graph.getNodes().contains(link.getSource())) {
				graph.getNodes().add(Node.with(baseRepository.findById(link.getSource().getId())));
			}
			if(!graph.getNodes().contains(link.getTarget())) {
				graph.getNodes().add(Node.with(baseRepository.findById(link.getTarget().getId())));
			}
		});

		List<Node> nodes = new ArrayList<>(graph.getNodes());
		for (int i = 0; i < nodes.size()/2; i++) {
			for (int j = nodes.size()-1; j >= nodes.size()/2; j--) {
				Node source = nodes.get(i);
				Node target = nodes.get(j);
				if(!graph.getLinks().contains(Link.with(source, target, "INHERIT")) && !graph.getLinks().contains(Link.with(target, source, "INHERIT"))) {
					BaseRelation relation = baseRepository.findInheritRelation(source.getId(), target.getId());
					if(Objects.nonNull(relation)) {
						graph.getLinks().add(Link.with(relation));
					}
				}
			}

		}
		return graph;
	}
}
