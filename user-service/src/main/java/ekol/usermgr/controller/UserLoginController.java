package ekol.usermgr.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ekol.usermgr.domain.UserLogin;
import ekol.usermgr.dto.UserLoginSearch;
import ekol.usermgr.repository.UserLoginRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users-login")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserLoginController {
	
	private UserLoginRepository userLoginRepository;
	
	@GetMapping
    public Page<UserLogin> searchUserLogin(UserLoginSearch search) {
    	return userLoginRepository.findAll(search.toSpecification(), search.toPage());
    }
	
	@GetMapping("/lookup/client-id")
	public List<String> listClientIds(@RequestParam(required = false) String username) {
		if(StringUtils.isBlank(username)) {
			return userLoginRepository.findDistinctClientId();
		}
		return userLoginRepository.findDistinctClientIdByUsername(username);
	}
}
