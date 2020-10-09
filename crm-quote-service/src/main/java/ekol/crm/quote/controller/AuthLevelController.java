package ekol.crm.quote.controller;

import ekol.crm.quote.service.AuthLevelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authLevel")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthLevelController {

    private AuthLevelService authLevelService;

    @GetMapping("/snapshot")
    public String createSnapshot() {
        authLevelService.updateOrInsertGraphUsersToCrmUsersTable();
        return "User list snapshot triggered";
    }

}
