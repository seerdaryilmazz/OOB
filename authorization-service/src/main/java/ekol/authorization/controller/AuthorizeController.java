package ekol.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.service.AuthorizeService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 28/02/2017.
 */
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizeController {

    private AuthorizeService authorizeService;

    @Timed(value = "authorization.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/authorize")
    public boolean authorize(@RequestParam String url, @RequestParam String method) {
        return authorizeService.authorize(url, method);
    }
    
    @Timed(value = "authorization.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/authorizeUi")
    public boolean authorize(@RequestParam String url) {
    	return authorizeService.authorizeUi(url);
    }
}
