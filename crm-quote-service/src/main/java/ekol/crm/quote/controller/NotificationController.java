package ekol.crm.quote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.service.QuoteNotification;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
	
	private QuoteNotification quoteNotification;
	
	@GetMapping("/notify-expiring-spot-quotes")
	public List<CodeNamePair> notifyExpiringSpotQuotes(
			@RequestParam(required = false, defaultValue = "2") Integer remainingDays, 
			@RequestParam(required = false, defaultValue = "EXPIRING_CRM_QUOTES") String concern) {
		return quoteNotification.proceedExpiringQuotesNotification(remainingDays, concern);
	}
	
	@GetMapping("/notify-won-spot-quotes-no-related-order")
	public List<CodeNamePair> notifyWonQuotesNoOrderNotification(
			@RequestParam(required = false, defaultValue = "7") Integer beforeDays, 
			@RequestParam(required = false, defaultValue = "WON_QUOTES_NO_RELATED_ORDER") String concern) {
		return quoteNotification.proceedWonQuotesNoOrderNotification(beforeDays, concern);
	}
}
