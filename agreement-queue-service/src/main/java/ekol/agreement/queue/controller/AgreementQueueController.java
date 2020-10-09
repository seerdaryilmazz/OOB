package ekol.agreement.queue.controller;

import ekol.agreement.queue.domain.RainbowQueueItem;
import ekol.agreement.queue.domain.dto.AgreementQueueSearchJson;
import ekol.agreement.queue.repository.RainbowQueueRepository;
import ekol.agreement.queue.service.AgreementQueueService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 24.09.2019
 */
@RestController
@RequestMapping("/agreement-queue")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AgreementQueueController {

    private AgreementQueueService agreementQueueService;
    private RainbowQueueRepository rainbowQueueRepository;

    @GetMapping
    public List<RainbowQueueItem> retrieveAgreement() {
        return rainbowQueueRepository.findAll();
    }

    @GetMapping("/search")
    public Page<RainbowQueueItem> retrieveQueueItems(AgreementQueueSearchJson searchJson){
        return agreementQueueService.search(searchJson);
    }

    @GetMapping("/agreement/{agreementId}")
    public Collection<RainbowQueueItem> findByAgreementIdAndSuccessfulStatus(@PathVariable Long agreementId){
        return agreementQueueService.findByAgreementIdAndSuccessfulStatus(agreementId);
    }

    @GetMapping("/requeue/{id}")
    public void requeueById(@PathVariable String id){
        agreementQueueService.export(id);
    }
}
