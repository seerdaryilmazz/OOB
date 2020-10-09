package ekol.crm.quote.controller;

import ekol.crm.quote.domain.dto.SpotQuotePdfSettingJson;
import ekol.crm.quote.domain.model.SpotQuotePdfSettingForListing;
import ekol.crm.quote.repository.SpotQuotePdfSettingForListingRepository;
import ekol.crm.quote.service.SpotQuotePdfSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/spot-quote-pdf-setting")
public class SpotQuotePdfSettingController {

    private SpotQuotePdfSettingForListingRepository listingRepository;

    private SpotQuotePdfSettingService service;

    @Autowired
    public SpotQuotePdfSettingController(
            SpotQuotePdfSettingForListingRepository listingRepository,
            SpotQuotePdfSettingService service) {
        this.listingRepository = listingRepository;
        this.service = service;
    }

    /**
     * Burada bilinçli olarak sayfalama yapmadık çünkü kayıtların belli bir sayıyı aşmayacağını düşünüyoruz.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<SpotQuotePdfSettingJson> findAll() {
        List<SpotQuotePdfSettingJson> list = new ArrayList<>();
        for (SpotQuotePdfSettingForListing elem : listingRepository.findAllByOrderByLanguageName()) {
            list.add(elem.toJson());
        }
        return list;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SpotQuotePdfSettingJson findById(@PathVariable Long id) {
        return service.findByIdOrThrowResourceNotFoundException(id).toJson();
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public SpotQuotePdfSettingJson create(@RequestBody SpotQuotePdfSettingJson requestDataJson) {
        return service.create(requestDataJson).toJson();
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public SpotQuotePdfSettingJson update(@PathVariable Long id, @RequestBody SpotQuotePdfSettingJson requestDataJson) {
        return service.update(id, requestDataJson).toJson();
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
