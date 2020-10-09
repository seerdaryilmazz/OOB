package ekol.authorization.controller;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.Subsidiary;
import ekol.authorization.repository.SubsidiaryRepository;
import ekol.authorization.service.SubsidiaryService;
import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/subsidiary")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SubsidiaryController {
    private SubsidiaryRepository subsidiaryRepository;
    private SubsidiaryService subsidiaryService;

    @GetMapping(value = "/{id}")
    public Subsidiary find(@PathVariable Long id) {
        return subsidiaryService.findByIdOrThrowResourceNotFoundException(id);
    }

    @GetMapping
    public List<Subsidiary> findAll() {
        return IterableUtils.toList(subsidiaryRepository.findAll());
    }

    @Authorize(operations = {"subsidiary.manage"})
    @PostMapping({"", "/"})
    public Subsidiary create(@RequestBody Subsidiary subsidiary) {

        if (subsidiary.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return subsidiaryService.createOrUpdate(subsidiary);
    }

    @Authorize(operations = {"subsidiary.manage"})
    @PutMapping({"/{id}", "/{id}/"})
    public Subsidiary update(@PathVariable Long id, @RequestBody Subsidiary subsidiary) {

        if (!id.equals(subsidiary.getId())) {
            throw new BadRequestException("Subsidiary.id must be " + id + ".");
        }

        return subsidiaryService.createOrUpdate(subsidiary);
    }

    @Authorize(operations = {"subsidiary.manage"})
    @DeleteMapping({"/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {
        subsidiaryService.softDelete(id);
    }

    @GetMapping("/by-company")
    public Subsidiary findByCompany(@RequestParam Long companyId) {
        return subsidiaryService.findByCompanyIdOrThrowResourceNotFoundException(companyId);
    }
}
