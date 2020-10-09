package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthSubsidiary;
import ekol.authorization.repository.auth.AuthSubsidiaryRepository;
import ekol.authorization.service.auth.AuthSubsidiaryService;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/subsidiary")
public class AuthSubsidiaryController {

    @Autowired
    private AuthSubsidiaryRepository authSubsidiaryRepository;

    @Autowired
    private AuthSubsidiaryService authSubsidiaryService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AuthSubsidiary find(@PathVariable Long id) {
        return authSubsidiaryService.findByIdOrThrowResourceNotFoundException(id);
    }

    /**
     * Kayıtları bir combobox'ta liste halinde göstermek istiyorsak SubsidiaryController.findAll daha doğru bir seçim olacaktır.
     * AuthSubsidiary, Subsidiary'nin Graph DB versiyonudur ve sadece relation tanımlayabilmek için kullanılıyor. Asıl sınıf Subsidiary'dir.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AuthSubsidiary> findAll() {
        return IterableUtils.toList(authSubsidiaryRepository.findAll());
    }
}
