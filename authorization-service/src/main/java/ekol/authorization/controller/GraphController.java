package ekol.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.dto.Graph;
import ekol.authorization.service.GraphService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/graph")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class GraphController {

	private GraphService graphService; 

	@GetMapping(value="/{id}")
	public Graph getGraphById(
			@PathVariable Long id, 
			@RequestParam(required=false, defaultValue="4") int depth, 
			@RequestParam(required=false) String direction) {
		return graphService.findById(id, depth, direction);
	}

	@GetMapping(value="/by")
	public Graph getGraphByExternalId(
			@RequestParam(required = true) Long externalId, 
			@RequestParam(required = false, defaultValue="4") int depth, 
			@RequestParam(required = false) String direction) {
		return graphService.findByExternalId(externalId, depth, direction);
	}
}
