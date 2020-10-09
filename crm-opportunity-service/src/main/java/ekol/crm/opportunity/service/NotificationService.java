package ekol.crm.opportunity.service;

import ekol.crm.opportunity.domain.dto.OpportunityJson;
import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.crm.opportunity.repository.OpportunityRepository;
import ekol.notification.api.NotificationApi;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 16.01.2020
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {

    private OpportunityRepository opportunityRepository;
    private NotificationApi notificationApi;

    public String checkExpectedQuoteDate(Integer days, String concern){
        LocalDate date = LocalDate.now().plusDays(days);
        List<OpportunityStatus> statuses = Arrays.asList(OpportunityStatus.PROSPECTING, OpportunityStatus.VALUE_PROPOSITION);
        List<Opportunity> opportunityList = opportunityRepository.findByExpectedQuoteDateBetweenAndStatusIn(LocalDate.now(), date, statuses);
        opportunityList.stream().map(OpportunityJson::fromEntity)
                .forEach(opportunity->notificationApi.sendNotification(concern, opportunity, opportunity.getOpportunityOwner()));
        return "OK";
    }
}
