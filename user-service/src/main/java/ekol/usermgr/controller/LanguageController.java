package ekol.usermgr.controller;

import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.exceptions.BadRequestException;
import ekol.usermgr.domain.Language;
import ekol.usermgr.repository.LanguageRepository;
import ekol.usermgr.service.LanguageService;

@RestController
@RequestMapping("/language")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @GetMapping({"", "/"})
    public List<Language> findAll() {
        return IteratorUtils.toList(languageRepository.findAllByOrderByNameAsc().iterator());
    }

    @GetMapping("/{id}")
    public Language find(@PathVariable Long id) {
        return languageService.findByIdOrThrowResourceNotFoundException(id);
    }

    @GetMapping("/iso/{isoCode}")
    public Language find(@PathVariable String isoCode) {
        return languageService.findByIsoCodeOrThrowResourceNotFoundException(isoCode);
    }

    @PostMapping({"", "/"})
    public Language create(@RequestBody Language language) {

        if (language != null && language.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return languageService.createOrUpdate(language);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Language update(@PathVariable Long id, @RequestBody Language language) {

        if (!id.equals(language.getId())) {
            throw new BadRequestException("Language.id must be " + id + ".");
        }

        return languageService.createOrUpdate(language);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {
        languageService.softDelete(id);
    }

}

