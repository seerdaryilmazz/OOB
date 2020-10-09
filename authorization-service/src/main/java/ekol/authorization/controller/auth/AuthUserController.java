package ekol.authorization.controller.auth;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.auth.AuthUser;
import ekol.authorization.dto.*;
import ekol.authorization.repository.auth.AuthUserRepository;
import ekol.authorization.service.MembershipService;
import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/user")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthUserController extends BaseController<AuthUser> {

	private SessionOwner sessionOwner;
    private AuthUserRepository authUserRepository;
    private MembershipService membershipService;
    
    @Override
    protected GraphRepository<AuthUser> getGraphRepository() {
        return authUserRepository;
    }

    @Override
    protected Class<AuthUser> getEntityClass() {
        return AuthUser.class;
    }

    @GetMapping("/byNodeIdAndLevel/{nodeId}/{level}")
    public List<AuthUser> findDirectMembersOfANodeByNodeIdAndLevel(@PathVariable("nodeId") Long nodeId, @PathVariable("level") Long level) {
        return membershipService.findDirectMembersOfANodeByNodeIdAndLevel(nodeId, level);
    }

    @GetMapping("/byNodeId/{nodeId}")
    public List<MemberOfRelation> findDirectMembersOfANodeByNodeId(@PathVariable("nodeId") Long nodeId) {
    	return membershipService.findDirectMembersOfANodeByNodeId(nodeId);
    }
    
    @GetMapping("/byNodeId/{nodeId}/active")
    public List<MemberOfRelation> findActiveMembersOfANodeByNodeId(@PathVariable("nodeId") Long nodeId) {
    	return membershipService.findActiveMembersOfANodeByNodeId(nodeId);
    }
    
    @GetMapping("/byNodeId/{nodeId}/subLevel")
	public List<MemberOfRelation> findSubLevelMembersOfANodeByNodeId(@PathVariable("nodeId") Long nodeId, @RequestParam String username) {
    	return  membershipService.findSubLevelMembersOfANodeByNodeId(nodeId, username);
	}
    
    @GetMapping("/byOperation")
    public List<AuthUser> findByAuthorizedOperation(@RequestParam String name){
    	return membershipService.findByAuthorizedOperation(name);
    }
    
    @GetMapping("/sublevel-users")
    public Map<String, Object> findTeamAndUser(@RequestParam(defaultValue = "true") Boolean getSubhierarchy){
    	return membershipService.listSublevelUsers(sessionOwner.getCurrentUser().getUsername(), getSubhierarchy);
    }
    
    @Authorize(operations = {"authorization.get-subhierarchy-of-user"})
    @PostMapping("/sublevel-users")
    public Map<String, Object> findTeamAndUser(@RequestBody Map<String, String> request, @RequestParam(defaultValue = "true") boolean getSubhierarchy){
    	return membershipService.listSublevelUsers(Optional.ofNullable(request.get("username")).orElseGet(sessionOwner.getCurrentUser()::getUsername), getSubhierarchy);
    }
    
    @PostMapping("/user-manager")
    public List<UserDto> listManagerOfUser(@RequestBody Map<String, String> request) {
    	return membershipService.listManagerOfUser(Optional.ofNullable(request.get("username")).orElseThrow(BadRequestException::new));
    }
    
}
