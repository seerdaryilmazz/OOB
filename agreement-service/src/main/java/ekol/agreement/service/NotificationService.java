package ekol.agreement.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.agreement.domain.dto.NotificationParametersJson;
import ekol.agreement.domain.enumaration.*;
import ekol.agreement.domain.model.OwnerInfo;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.repository.AgreementRepository;
import ekol.model.*;
import ekol.notification.api.NotificationApi;
import ekol.notification.dto.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {
	
	private static final Set<ResponsibilityType> RESPONSIBLES = new HashSet<>(Arrays.asList(ResponsibilityType.INFORMED, ResponsibilityType.RESPONSIBLE));
	
	private AgreementRepository agreementRepository;
	private NotificationApi notificationApi;

	public Collection<CodeNamePair> notify(NotificationParametersJson parameters) {
		LocalDate today = LocalDate.now();
		LocalDate date = today.plusDays(parameters.getDay());
		Map<Long, Agreement> agreements = new HashMap<>();
		if(Objects.equals("agreemendEndDate", parameters.getJobName())) {
			agreementRepository.findByEndDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("insuranceEndDate", parameters.getJobName())) {
			agreementRepository.findByInsuranceInfos_validityEndDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("financialInfoStampTaxDueDate", parameters.getJobName())) {
			agreementRepository.findByStampTaxDueDateForNotify(today, date, AgreementStatus.CANCELED, StampTaxPayer.CUSTOMER, Boolean.FALSE).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("autoRenewalDate", parameters.getJobName())) {
			agreementRepository.findByAutoRenewalDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("letterOfGuaranteeValidityEndDate", parameters.getJobName())) {
			agreementRepository.findByLetterOfGuarentees_validityEndDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("unitPriceValidityEndDate", parameters.getJobName())) {
			agreementRepository.findByUnitPrices_validityEndDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		} else if(Objects.equals("kpiInfoNextUpdateDate", parameters.getJobName())) {
			agreementRepository.findByKpiInfos_nextUpdateDateBetweenAndStatusIsNot(today, date, AgreementStatus.CANCELED).forEach(agreement->agreements.putIfAbsent(agreement.getId(), agreement));
		}
		agreements.values().forEach(agreement->this.sendNotification(agreement, parameters.getConcern()));
		return agreements.values().parallelStream().map(agreement->CodeNamePair.with(String.valueOf(agreement.getNumber()), agreement.getName())).collect(Collectors.toSet());
	}
	
	public Collection<String> jobNames(){
		return Arrays.asList(
				"agreemendEndDate",
				"insuranceEndDate",
				"financialInfoStampTaxDueDate",
				"autoRenewalDate",
				"letterOfGuaranteeValidityEndDate",
				"unitPriceValidityEndDate",
				"kpiInfoNextUpdateDate");
	}
	
	private List<Notification> sendNotification(Agreement agreement, String concern) {
		Long[] userIds = agreement.getOwnerInfos().parallelStream().filter(t->RESPONSIBLES.contains(t.getResponsibilityType())).map(OwnerInfo::getName).map(IdNamePair::getId).distinct().toArray(Long[]::new);
		return notificationApi.sendNotification(concern, ()->NotificationBuild.with(agreement.toJson(), userIds));
	}

}
