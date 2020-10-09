package ekol.orders.lookup.controller;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.orders.lookup.domain.CustomsOperationType;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.lookup.repository.AdrPackageTypeRepository;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.lookup.repository.PackageGroupRepository;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import ekol.orders.order.domain.dto.json.CreateOrderLookupResponseJson;
import ekol.orders.transportOrder.domain.CurrencyType;
import ekol.orders.transportOrder.domain.VehicleFeature;
import ekol.orders.transportOrder.repository.EquipmentTypeRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/for-create-order")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CreateOrderLookupController {

    private PaymentMethodRepository paymentMethodRepository;
    private IncotermRepository incotermRepository;
    private PackageTypeRepository packageTypeRepository;
    private PackageGroupRepository packageGroupRepository;
    private EquipmentTypeRepository equipmentTypeRepository;
    private DocumentTypeRepository documentTypeRepository;
    private AdrPackageTypeRepository adrPackageTypeRepository;

    @GetMapping
    public CreateOrderLookupResponseJson getLookups() {
        CreateOrderLookupResponseJson response = new CreateOrderLookupResponseJson();
        response.setLoadTypes(Arrays.asList(TruckLoadType.values()));
        response.setServiceTypes(Arrays.asList(ServiceType.values()));
        response.setPaymentMethods(paymentMethodRepository.findAll());
        response.setIncoterms(incotermRepository.findAll());
        response.setPackageTypes(packageTypeRepository.findByOrderByRankAscNameAsc());
        response.setPackageGroups(packageGroupRepository.findAll());
        response.setAdrPackageTypes(adrPackageTypeRepository.findAll());
        response.setCurrencyTypes(Arrays.asList(CurrencyType.values()));
        response.setVehicleFeatures(Arrays.asList(VehicleFeature.values()));
        response.setEquipmentTypes(equipmentTypeRepository.findAll());
        response.setCustomsOperationTypes(Arrays.asList(CustomsOperationType.values()));
        Iterable<DocumentType> documentTypes = documentTypeRepository.findAll();
        response.setHealthCertificateDocumentTypes(
                StreamSupport.stream(documentTypes.spliterator(), false)
                        .filter(documentType -> documentType.getDocumentGroup().equals(DocumentTypeGroup.HEALTH_CERTIFICATE))
                        .collect(toList()));
        response.setAdrDocumentTypes(
                StreamSupport.stream(documentTypes.spliterator(), false)
                        .filter(documentType -> documentType.getDocumentGroup().equals(DocumentTypeGroup.DANGEROUS_GOODS))
                        .collect(toList()));
        response.setOtherDocumentTypes(
                StreamSupport.stream(documentTypes.spliterator(), false)
                        .filter(
                        documentType -> !documentType.getDocumentGroup().equals(DocumentTypeGroup.DANGEROUS_GOODS) &&
                                !documentType.getDocumentGroup().equals(DocumentTypeGroup.HEALTH_CERTIFICATE))
                        .collect(toList()));
        return response;
    }
}
