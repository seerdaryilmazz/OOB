package ekol.agreement.validation;

import ekol.agreement.domain.dto.agreement.*;
import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.domain.enumaration.ResponsibilityType;
import ekol.exceptions.ParameterizedRuntimeException;
import ekol.resource.validation.Validator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ValidationException;

/**
 * Created by Dogukan Sahinturk on 3.02.2020
 */
@Component
public class SaveAgreementValidation implements Validator {
    @Override
    public void validate(Object[] args) throws ParameterizedRuntimeException {
        AgreementJson agreementJson;
        if(args[0] instanceof AgreementJson){
            agreementJson = AgreementJson.class.cast(args[0]);
        }else {
            Long id = Long.class.cast(args[0]);
            agreementJson = AgreementJson.class.cast(args[1]);
        }

        if(AgreementStatus.APPROVED == agreementJson.getStatus() && CollectionUtils.isEmpty(agreementJson.getUnitPrices())){
            throw new ValidationException("The Agreement can not be approved without any unit price");
        }
        if(agreementJson.getEndDate().isBefore(agreementJson.getStartDate())){
            throw new ValidationException("The Agreement's start date must be earlier than end date");
        }
        if (CollectionUtils.isEmpty(agreementJson.getOwnerInfos()) && agreementJson.getOwnerInfos().stream().noneMatch(i -> ResponsibilityType.RESPONSIBLE == i.getResponsibilityType())) {
            throw new ValidationException("Agreement should have at least one Responsible!");
        }

        if(!CollectionUtils.isEmpty(agreementJson.getPriceAdaptationModels())){
            agreementJson.getPriceAdaptationModels().stream().forEach(this::validatePriceAdaptationModels);
        }
        if(!CollectionUtils.isEmpty(agreementJson.getLetterOfGuarentees())){
            agreementJson.getLetterOfGuarentees().stream().forEach(this::validateLetterOfGuarantees);
        }
        if(!CollectionUtils.isEmpty(agreementJson.getInsuranceInfos())){
            agreementJson.getInsuranceInfos().stream().forEach(this::validateInsuranceInfos);
        }
    }

    public void validatePriceAdaptationModels(PriceAdaptationModelJson priceAdaptationModelJson){
        if(priceAdaptationModelJson.getEur()>100){
            throw new ValidationException("EUR (%) can not be bigger than %100");
        }
        if(priceAdaptationModelJson.getUsd()>100){
            throw new ValidationException("USD (%) can not be bigger than %100");
        }
        if(priceAdaptationModelJson.getInflation()>100){
            throw new ValidationException("Inflation (%) can not be bigger than %100");
        }
        if(priceAdaptationModelJson.getMinimumWage()>100){
            throw new ValidationException("Minimum Wage (%) can not be bigger than %100");
        }
        if (priceAdaptationModelJson.getMinimumWage() + priceAdaptationModelJson.getInflation() + priceAdaptationModelJson.getUsd() + priceAdaptationModelJson.getEur() != 100) {
            throw new ValidationException("Sum of  EUR (%) , USD (%), Inflation (%) and Minimum Rage (%) values must be equal to 100!");
        }
    }

    public void validateLetterOfGuarantees(LetterOfGuarenteeJson letterOfGuarenteeJson){
        if(letterOfGuarenteeJson.getValidityEndDate().isBefore(letterOfGuarenteeJson.getValidityStartDate())){
            throw new ValidationException("Letter of guarantee's validity start date must be earlier than validity end date");
        }
    }

    public void validateInsuranceInfos(InsuranceInfoJson insuranceInfoJson){
        if(insuranceInfoJson.getValidityEndDate().isBefore(insuranceInfoJson.getValidityStartDate())){
            throw new ValidationException("Insurance Info's validity start date must be earlier than validity end date");
        }
    }
}
