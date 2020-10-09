package ekol.usermgr.controller;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.usermgr.domain.*;
import ekol.usermgr.dto.*;
import ekol.usermgr.repository.UserRepository;
import ekol.usermgr.service.UserService;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 13/04/16.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private static final String ERROR_MESSAGE= "User with id {0} not found";

    private UserRepository userRepository;
    private UserService userService;

    private void validateActiveUser(String username){
        if(!userRepository.existsByUsernameAndStatus(username, UserStatus.ACTIVE)){
            throw new ResourceNotFoundException(ERROR_MESSAGE, username);
        }
    }
    private void validateUser(String username){
        if(!userRepository.existsByUsername(username)){
            throw new ResourceNotFoundException(ERROR_MESSAGE, username);
        }
    }
    private void validateUser(Long id){
    	if(!userRepository.exists(id)){
    		throw new ResourceNotFoundException(ERROR_MESSAGE, id);
    	}
    }
    
    @Authorize(operations = "user.manage")
    @PostMapping
    public User add(@RequestBody User user){
        return userService.save(user);
    }

    @Authorize(operations = "user.manage")
    @PutMapping("/{username:.+}")
    public User update(@RequestBody User user, @PathVariable String username){
        validateActiveUser(username);
        return userService.updateUserAggregate(username, user);
    }

    @PostMapping("/checkPassword")
    public boolean checkPassword(@RequestBody Map<String, Object> params) {
        return userService.checkPassword((String) params.get("username"), (String) params.get("password"));
    }
    
    @GetMapping("/checkUsername")
    public UserResponse checkUsername(@RequestParam String username) {
        validateActiveUser(username);
        return Optional.ofNullable(userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE)).map(UserResponse::fromUser).orElse(null);
    }

    @Authorize(operations = "user.manage")
    @DeleteMapping("/{username:.+}")
    public User delete( @PathVariable String username){
        validateUser(username);
        User user = userRepository.findByUsername(username);
        user.setDeleted(true);
        return userRepository.save(user);
    }

    @Authorize(operations = {"user.get-list", "user.manage"})
    @GetMapping
    public Object find(@RequestParam(required = false) Long id){
    	if(Objects.nonNull(id)) {
    		validateUser(id);
        	return Optional.ofNullable(userRepository.findOne(id)).map(UserResponse::fromUser).orElse(null);
    	}
        return userRepository.findAll();
    }
    
    @PostMapping("/by-username")
    public Iterable<UserResponse> findByUsernames(
    		@RequestBody List<String> usernames, 
    		@RequestParam(required = false) UserStatus[] status,
    		@RequestParam(required = false) UserAuthenticationType[] authenticationType
    		){
    	UserStatus[] statusFilter = Optional.ofNullable(status).filter(ArrayUtils::isNotEmpty).orElseGet(UserStatus::values);
    	UserAuthenticationType[] typeFilter = Optional.ofNullable(authenticationType).filter(ArrayUtils::isNotEmpty).orElseGet(UserAuthenticationType::values);
    	return StreamSupport.stream(userRepository.findByUsernameInAndStatusInAndAuthenticationTypeIn(usernames, Arrays.asList(statusFilter), Arrays.asList(typeFilter)).spliterator(),false)
    			.map(UserResponse::fromUser)
    			.collect(Collectors.toList()) ;
    }

    @Authorize(operations = "user.manage")
    @GetMapping("/search")
    public Iterable<User> search(@RequestParam(required = false) String username,
                                 @RequestParam(required = false) String displayName,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) boolean inactiveUsers,
                                 @RequestParam(required=false) UserAuthenticationType authenticationType){
        return userService.search(UserSearchFilter.createWith(username, displayName, email, inactiveUsers, authenticationType));
    }

    @GetMapping("/list")
    public Iterable<UserResponse> list(){
    	return StreamSupport.stream(search(null, null, null, true, UserAuthenticationType.ACTIVE_DIRECTORY).spliterator(), true).map(UserResponse::fromUser).collect(Collectors.toSet());
    }

    @GetMapping("/{username:.+}")
    public UserResponse find(@PathVariable String username){
        validateUser(username);
        return Optional.ofNullable(userRepository.findByUsername(username)).map(UserResponse::fromUser).orElse(null);
    }

    @Authorize(operations = "user.manage")
    @PutMapping("/{username:.+}/activate")
    public void activate(@PathVariable String username){
        validateUser(username);
        userService.activate(username);
    }

    @Authorize(operations = "user.manage")
    @PutMapping("/{username:.+}/deactivate")
    public void deactivate(@PathVariable String username){
        validateUser(username);
        userService.deactivate(username);
    }

    @GetMapping("/current")
    public ekol.model.User findCurrent() {
    	return Optional.ofNullable(userService.retrieveSessionOwner()).orElseThrow(()->new BadRequestException("There is no user session."));
    }

    @GetMapping("/disable-not-existed-user")
    public String disableUsers() {
        userService.disableUser();
        return "OK";
    }
}
