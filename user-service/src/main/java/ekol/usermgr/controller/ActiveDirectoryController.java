package ekol.usermgr.controller;


import ekol.usermgr.service.ActiveDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.ResourceNotFoundException;
import ekol.usermgr.domain.ActiveDirectory;
import ekol.usermgr.dto.ActiveDirectoryUser;

/**
 * Created by kilimci on 07/04/16.
 */
@RestController
@RequestMapping("/active-directory")
public class ActiveDirectoryController {

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @GetMapping("/{username:.+}/exists")
    public void userExists(@PathVariable String username) {
        boolean exists = activeDirectoryService.userExists(username);
        if (!exists) {
            throw new ResourceNotFoundException("username {0} not found", username);
        }
    }

    @Authorize(operations = "user.manage")
    @GetMapping("/{username:.+}/details")
    public ActiveDirectoryUser userDetails(@PathVariable String username) {
        return activeDirectoryService.userDetails(username);
    }
}
