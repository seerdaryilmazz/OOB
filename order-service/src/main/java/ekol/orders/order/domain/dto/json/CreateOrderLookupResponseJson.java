package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.lookup.domain.*;
import ekol.orders.transportOrder.domain.CurrencyType;
import ekol.orders.transportOrder.domain.EquipmentType;
import ekol.orders.transportOrder.domain.VehicleFeature;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderLookupResponseJson {

    private Iterable<TruckLoadType> loadTypes = new ArrayList<>();
    private Iterable<ServiceType> serviceTypes = new ArrayList<>();
    private Iterable<PaymentMethod> paymentMethods = new ArrayList<>();
    private Iterable<Incoterm> incoterms = new ArrayList<>();
    private Iterable<PackageType> packageTypes = new ArrayList<>();
    private Iterable<PackageGroup> packageGroups = new ArrayList<>();
    private Iterable<AdrPackageType> adrPackageTypes = new ArrayList<>();
    private Iterable<CurrencyType> currencyTypes = new ArrayList<>();
    private Iterable<EquipmentType> equipmentTypes = new ArrayList<>();
    private Iterable<VehicleFeature> vehicleFeatures = new ArrayList<>();
    private Iterable<CustomsOperationType> customsOperationTypes = new ArrayList<>();
    private Iterable<DocumentType> healthCertificateDocumentTypes = new ArrayList<>();
    private Iterable<DocumentType> adrDocumentTypes = new ArrayList<>();
    private Iterable<DocumentType> otherDocumentTypes = new ArrayList<>();

    public Iterable<TruckLoadType> getLoadTypes() {
        return loadTypes;
    }

    public void setLoadTypes(Iterable<TruckLoadType> loadTypes) {
        this.loadTypes = loadTypes;
    }

    public Iterable<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(Iterable<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Iterable<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Iterable<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Iterable<Incoterm> getIncoterms() {
        return incoterms;
    }

    public void setIncoterms(Iterable<Incoterm> incoterms) {
        this.incoterms = incoterms;
    }

    public Iterable<PackageType> getPackageTypes() {
        return packageTypes;
    }

    public void setPackageTypes(Iterable<PackageType> packageTypes) {
        this.packageTypes = packageTypes;
    }

    public Iterable<CurrencyType> getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(Iterable<CurrencyType> currencyTypes) {
        this.currencyTypes = currencyTypes;
    }

    public Iterable<EquipmentType> getEquipmentTypes() {
        return equipmentTypes;
    }

    public void setEquipmentTypes(Iterable<EquipmentType> equipmentTypes) {
        this.equipmentTypes = equipmentTypes;
    }

    public Iterable<VehicleFeature> getVehicleFeatures() {
        return vehicleFeatures;
    }

    public void setVehicleFeatures(Iterable<VehicleFeature> vehicleFeatures) {
        this.vehicleFeatures = vehicleFeatures;
    }

    public Iterable<CustomsOperationType> getCustomsOperationTypes() {
        return customsOperationTypes;
    }

    public void setCustomsOperationTypes(Iterable<CustomsOperationType> customsOperationTypes) {
        this.customsOperationTypes = customsOperationTypes;
    }

    public Iterable<DocumentType> getHealthCertificateDocumentTypes() {
        return healthCertificateDocumentTypes;
    }

    public void setHealthCertificateDocumentTypes(Iterable<DocumentType> healthCertificateDocumentTypes) {
        this.healthCertificateDocumentTypes = healthCertificateDocumentTypes;
    }

    public Iterable<DocumentType> getAdrDocumentTypes() {
        return adrDocumentTypes;
    }

    public void setAdrDocumentTypes(Iterable<DocumentType> adrDocumentTypes) {
        this.adrDocumentTypes = adrDocumentTypes;
    }

    public Iterable<DocumentType> getOtherDocumentTypes() {
        return otherDocumentTypes;
    }

    public void setOtherDocumentTypes(Iterable<DocumentType> otherDocumentTypes) {
        this.otherDocumentTypes = otherDocumentTypes;
    }

    public Iterable<PackageGroup> getPackageGroups() {
        return packageGroups;
    }

    public void setPackageGroups(Iterable<PackageGroup> packageGroups) {
        this.packageGroups = packageGroups;
    }

    public Iterable<AdrPackageType> getAdrPackageTypes() {
        return adrPackageTypes;
    }

    public void setAdrPackageTypes(Iterable<AdrPackageType> adrPackageTypes) {
        this.adrPackageTypes = adrPackageTypes;
    }
}
