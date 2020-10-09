package ekol.authorization.controller;

import ekol.authorization.domain.auth.AuthHomepage;
import ekol.authorization.repository.auth.AuthHomepageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/homepage")
public class HomePageController {

    @Autowired
    private AuthHomepageRepository repository;

    @RequestMapping(value = "/{username:.+}", method = {RequestMethod.GET})
    public List<String> getDepartmentHomepage(@PathVariable String username) {
        Collection<AuthHomepage> homepages = repository.retrieveUserHomepages(username, LocalDate.now().toEpochDay());
        return homepages.stream().map(AuthHomepage::getUrl).collect(toList());
    }
}
