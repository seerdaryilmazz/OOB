package ekol.agreement.service;

import ekol.agreement.domain.model.HistoryUnitPrice;
import ekol.agreement.repository.HistoryUnitPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dogukan Sahinturk on 1.10.2019
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeleteService {

    private AgreementService agreementService;
    private HistoryUnitPriceRepository historyUnitPriceRepository;

    @Transactional
    public void deleteHistoriesOfUnitPriceByUnitPriceId(Long id){
        List<HistoryUnitPrice> historyUnitPrices = agreementService.retrieveHistoryUnitPricesByUnitPriceId(id);
        historyUnitPrices.forEach(historyUnitPrice -> {
            historyUnitPrice.setDeleted(Boolean.TRUE);
            historyUnitPriceRepository.save(historyUnitPrice);
        });
    }

    @Transactional
    public void deleteHistoryOfUnitPriceById(Long id){
        HistoryUnitPrice historyUnitPrice = historyUnitPriceRepository.findOne(id);
        historyUnitPrice.setDeleted(Boolean.TRUE);
        historyUnitPriceRepository.save(historyUnitPrice);
    }
}
