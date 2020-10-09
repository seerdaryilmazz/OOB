package ekol.authorization.controller.userdetails;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.auth.*;
import ekol.authorization.domain.dto.UserMemberships;
import ekol.authorization.dto.IdNamePair;
import ekol.authorization.service.userdetails.UserDetailsService;
import ekol.event.auth.Authorize;
import ekol.resource.oauth2.SessionOwner;
import io.micrometer.core.annotation.Timed;

/**
 * Created by ozer on 28/02/2017.
 */
@RestController
@RequestMapping("/user-details")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SessionOwner sessionOwner;

    @GetMapping("/subsidiaries")
    public Set<IdNamePair> listSubsidiaries(@RequestParam String username) {
        return userDetailsService.retrieveUserSubsidiaries(username);
    }

    @GetMapping("/subsidiaries-current-user")
    public Set<IdNamePair> listSubsidiaries() {
        return userDetailsService.retrieveUserSubsidiaries(sessionOwner.getCurrentUser().getUsername());
    }
    
    @Timed(value = "authorization.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/active-memberships")
    public UserMemberships listActiveMemberships(@RequestParam String username) {
        return UserMemberships.createWith(userDetailsService.findUserWithActiveMemberships(username));
    }

    @GetMapping("/locations")
    public Set<IdNamePair> listLocations(@RequestParam String username) {
        return userDetailsService.retrieveUserLocations(username);
    }

    @GetMapping("/{username:.+}")
    public AuthUser listMemberships(@PathVariable String username) {
        return userDetailsService.findUser(username);
    }

    @PutMapping("/{username:.+}")
    public AuthUser saveUser(@PathVariable String username, @RequestBody AuthUser user) {
        return userDetailsService.saveUser(username, user);
    }

    @PutMapping("/{username:.+}/memberships")
    public void saveUserMemberships(@PathVariable String username, @RequestBody Set<MemberOfRelation> memberships) {
        userDetailsService.saveUserMemberships(username, memberships);
    }
    
    @GetMapping("/{username:.+}/teammates")
    public Iterable<AuthUser> retrieveTeammates(@PathVariable String username){
    	return userDetailsService.retrieveTeammates(username);
    }
}
