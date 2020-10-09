package ekol.authorization.controller.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.dto.*;
import ekol.authorization.service.auth.AuthLevelService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AuthLevelController {

    private AuthLevelService authLevelService;

    @GetMapping("/getUserList/{departmentCode:.+}")
    public List<User> getUserList(@PathVariable String departmentCode) {
        return authLevelService.prepareUserList(departmentCode);
    }
    
    @GetMapping("/level-of/user/{username:.+}")
    public List<AuthLevelDto> getAuthLevelsOfUser(@PathVariable String username) {
    	return authLevelService.listTeamInheritenceByUser(username).stream()
				.map(t -> AuthLevelDto.with(
						new IdNamePair(t.getUser().getExternalId(), t.getUser().getName()),
						t.getNodes().stream().map(Node::with).collect(Collectors.toList())))
				.collect(Collectors.toList());
    }
}
