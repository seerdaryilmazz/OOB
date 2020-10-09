package ekol.crm.opportunity.controller;

import ekol.crm.opportunity.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Dogukan Sahinturk on 16.01.2020
 */
@RestController
@RequestMapping("/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {

    private NotificationService notificationService;

    @GetMapping("/notify-for-expected-quote-date-ending")
    public String notifyForExpectedQuoteDateEnding(
            @RequestParam(defaultValue = "30", required = false) Integer days,
            @RequestParam(defaultValue = "OPPORTUNITY_EXPECTED_QUOTE_DATE", required = false) String concern){
        return notificationService.checkExpectedQuoteDate(days, concern);
    }
}
