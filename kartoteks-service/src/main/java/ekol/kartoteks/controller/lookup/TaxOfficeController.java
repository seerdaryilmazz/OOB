package ekol.kartoteks.controller.lookup;

import ekol.kartoteks.domain.TaxOffice;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import ekol.kartoteks.service.TaxOfficeUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kilimci on 10/01/2017.
 */
@RestController
@RequestMapping("/tax-office")
public class TaxOfficeController {
    @Autowired
    private TaxOfficeRepository taxOfficeRepository;

    @Autowired
    private TaxOfficeUpdateService taxOfficeUpdateService;

    @RequestMapping(value = "/updateAll", method = RequestMethod.GET)
    public void updateAll() {
        taxOfficeUpdateService.updateAll();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TaxOffice> lisTaxOffices() {
        return taxOfficeRepository.findAllByOrderByNameAsc();
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public TaxOffice getTaxOffice(@PathVariable String code) {
        return taxOfficeRepository.findByCode(code);
    }
}
