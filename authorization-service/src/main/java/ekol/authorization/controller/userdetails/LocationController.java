package ekol.authorization.controller.userdetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.auth.AuthUser;
import ekol.authorization.repository.auth.AuthLocationRepository;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 07/09/2017.
 */
@RestController
@RequestMapping("/location")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LocationController {
    
    private AuthLocationRepository authLocationRepository;

    @GetMapping("/{locationId}/users")
    public List<AuthUser> listUsersAtLocation(@PathVariable Long locationId) {
        return authLocationRepository.getUsersOfLocation(locationId);
    }


}
