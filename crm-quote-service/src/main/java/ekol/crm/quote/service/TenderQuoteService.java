package ekol.crm.quote.service;

import java.util.ArrayList;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.EmailMessage;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.BadRequestException;

@Service
public class TenderQuoteService extends AbstractQuoteService {

	@Override
	public void emailQuote(Quote quote, EmailJson request) {

		if (CollectionUtils.isEmpty(request.getTo())) {
			throw new BadRequestException("At least one to address must be specified.");
		}

		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setReplyTo(currentUser().getEmail());
		emailMessage.setTo(request.getTo());
		emailMessage.setCc(request.getCc());
		emailMessage.setBcc(request.getBcc());
		emailMessage.setSubject(request.getSubject());
		emailMessage.setAttachments(new ArrayList<>());
		emailMessage.setHtml(Boolean.TRUE);
		emailMessage.setBody(getPdfService().createMailForTenderQuote(TenderQuote.class.cast(quote), 1L, request.getBody()));
		sendEmail(quote, emailMessage);
	}

	@Override
	protected void createPdf(Quote quote) {
		// TODO Auto-generated method stub
		
	}

}
