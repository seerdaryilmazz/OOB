package ekol.authorization.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.*;
import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.dto.Node;
import ekol.authorization.service.TeamService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/team")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class TeamController {
	
	private TeamService teamService;
	
	@Authorize(operations="team.manage")
	@PostMapping({"/", ""})
    public Team add(@RequestBody Team team){
        return teamService.save(team);
    }

    @GetMapping({"/", ""})
    public Iterable<Team> findAll(@RequestParam(required=false, value="status") List<EntityStatus> status){
        return teamService.findAllByStatus(status);

    }

    @GetMapping({"/{id}/", "/{id}"})
    public Team get(@PathVariable Long id){
        return teamService.findTeamOrThrowException(id);
    }
    @GetMapping({"/by-name/{name}/", "/by-name/{name}"})
    public Team getByName(@PathVariable String name, @RequestParam(defaultValue="false", required=false) boolean ignoreCase){
    	return teamService.findTeamByName(name, ignoreCase);
    }

    @Authorize(operations="team.manage")
    @PutMapping({"/{id}/", "/{id}"})
    public Team update(@PathVariable Long id, @RequestBody Team team){
        return teamService.update(team, id);
    }

    @Authorize(operations="team.manage")
    @PutMapping({"/{id}/status"})
    public Team updateStatus(@PathVariable Long id, @RequestBody Team team){
    	return teamService.updateStatus(team, id);
    }

    @Authorize(operations= {"team.manage"})
    @DeleteMapping({"/{id}/", "/{id}"})
    public void delete(@PathVariable Long id){
        teamService.delete(id);
    }
    
    @GetMapping({"/{externalId}/node"})
    public AuthTeam getNode(@PathVariable Long externalId) {
    	return teamService.findNodeByExternalIdOrThrowException(externalId, 0);
    }
    
    @GetMapping({"/by-username/{username:.+}"})
    public Iterable<Team> listByMemberOfLevelAndUser(@PathVariable String username) {
    	return teamService.findByMemberOfLevelAndUser(username);
    }
    
    @GetMapping({"/by-username/{username:.+}/of-departments"})
    public Iterable<Team> findTeamByCurrentDepartmentOfUser(@PathVariable String username) {
    	return teamService.findTeamByCurrentDepartmentOfUser(username);
    }
    
    @GetMapping("/with-hierarchy")
    public Set<Node> listTeamsWithHierarchy(@RequestParam(required=false, value="status") List<EntityStatus> status){
    	return teamService.listTeamWithHierarchy(status);
    }
    
    @GetMapping({"/by-username/{username:.+}/with-hierarchy"})
    public Iterable<Node> listByMemberOfLevelAndUserWithHierarchy(@PathVariable String username) {
    	return teamService.findByMemberOfLevelAndUserWithHierarchy(username);
    }
    
    @GetMapping({"/by-username/{username:.+}/of-departments/with-hierarchy"})
    public Iterable<Node> findTeamByCurrentDepartmentOfUserWithHierarchy(@PathVariable String username){
    	return teamService.findTeamByCurrentDepartmentOfUserWithHierarchy(username);
    }
}
