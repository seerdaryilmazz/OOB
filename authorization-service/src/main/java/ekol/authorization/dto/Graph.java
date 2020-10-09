package ekol.authorization.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Graph {

	private Set<Node> nodes;
	private Set<Link> links; 
	
	public static Graph with(Set<Node> nodes, Set<Link> links) {
		Graph graph = new Graph();
		graph.setLinks(links);
		graph.setNodes(nodes);
		return graph;
	}
	public static Graph with() {
		return with(new HashSet<>(), new HashSet<>());
	}
	
	public Graph merge(Graph graph) {
		nodes.addAll(graph.getNodes());
		links.addAll(graph.getLinks());
		return this;
	}
}
