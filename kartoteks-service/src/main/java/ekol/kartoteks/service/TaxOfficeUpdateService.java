package ekol.kartoteks.service;


import com.google.common.collect.Lists;
import ekol.kartoteks.domain.TaxOffice;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kilimci on 20/10/16.
 */
@Service
public class TaxOfficeUpdateService {

    @Autowired
    private TaxOfficeRepository taxOfficeRepository;

    @Value("${updateTaxOffice.host:tax-office-service}")
    private String taxOfficeHost;

    private static final String KEY_NAME = "vdadi";
    private static final String KEY_CODE = "kod";

    private RestTemplate restTemplate = new RestTemplate();

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Scheduled(cron = "0 0 8 * * MON")
    public void runTaxOfficeUpdate(){
        updateAll();
    }

    @Transactional
    public void updateAll(){
        HashMap<String, List<Map<String, String>>> updatedTaxOffices = restTemplate.getForObject(taxOfficeHost, HashMap.class);
        List<TaxOffice> existingTaxOffices = Lists.newArrayList(taxOfficeRepository.findAll());

        updatedTaxOffices.forEach((cityCode, taxOffices) -> {
            if(!taxOffices.isEmpty()){
                List<TaxOffice> existingTaxOfficesByCity = filterByCityCode(cityCode, existingTaxOffices);
                setAllDeleted(existingTaxOfficesByCity);
                updateAllExists(taxOffices, existingTaxOfficesByCity);
                existingTaxOfficesByCity.addAll(createAllNew(cityCode, taxOffices, existingTaxOfficesByCity));
                existingTaxOfficesByCity.forEach(taxOfficeRepository::save);
            }
        });
    }
    private List<TaxOffice> createAllNew(String cityCode, List<Map<String, String>> updatedTaxOffices, List<TaxOffice> existingTaxOffices){
        return updatedTaxOffices.stream().filter(updatedTaxOffice ->
                existingTaxOffices.stream().filter(existingTaxOffice -> existingTaxOffice.getCode().equals(updatedTaxOffice.get(KEY_CODE)))
                        .collect(Collectors.toList()).isEmpty()
        ).map(newTaxOffice -> createNewTaxOffice(cityCode, newTaxOffice)).collect(Collectors.toList());

    }

    private List<TaxOffice> filterByCityCode(String cityCode, List<TaxOffice> existingTaxOffices){
        return existingTaxOffices.stream().filter(taxOffice -> taxOffice.getCityCode().equals(cityCode)).collect(Collectors.toList());
    }

    private void setAllDeleted(List<TaxOffice> existingTaxOffices){
        existingTaxOffices.forEach(taxOffice -> taxOffice.setDeleted(true));
    }

    private TaxOffice createNewTaxOffice(String cityCode, Map<String, String> taxOffice){
        TaxOffice newTaxOffice = new TaxOffice();
        newTaxOffice.setCityCode(cityCode);
        newTaxOffice.setCode(taxOffice.get("kod"));
        newTaxOffice.setCountryCode("TR");
        newTaxOffice.setName(taxOffice.get("vdadi"));
        return newTaxOffice;
    }
    private void updateAllExists(List<Map<String, String>> updatedTaxOffices, Iterable<TaxOffice> existingTaxOffices){
        existingTaxOffices.forEach(existingTaxOffice ->
                updatedTaxOffices.forEach(updatedTaxOffice -> {
                    if(existingTaxOffice.getCode().equals(updatedTaxOffice.get(KEY_CODE))){
                        existingTaxOffice.setDeleted(false);
                        existingTaxOffice.setName(updatedTaxOffice.get(KEY_NAME));
                    }
                })
        );
    }
}
