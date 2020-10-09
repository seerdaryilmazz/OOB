package ekol.location.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.Country;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.dto.SearchWarehouseJson;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.repository.CountryRepository;
import ekol.location.repository.CustomerWarehouseRepository;
import ekol.location.repository.CustomsDetailsRepository;
import ekol.location.repository.LocationRepository;
import ekol.location.repository.specs.CustomerWarehouseSpecification;
import ekol.location.validator.CustomerWarehouseValidator;

/**
 * Created by burak on 05/04/17.
 */
@Service
public class CustomerWarehouseService {

    @Autowired
    private CustomerWarehouseRepository customerWarehouseRepository;

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CustomsDetailsRepository customsDetailsRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerWarehouseValidator customerWarehouseValidator;

    @Autowired
    private CustomsOfficeService customsOfficeService;

    @Transactional
    public void addCustomerWarehouse(CustomerWarehouse customerWarehouse) {

        if (customerWarehouse.getId() != null) {
            throw new BadRequestException("New Customer Warehouse should not have id");
        }

        customerWarehouseValidator.validate(customerWarehouse);

        CustomerWarehouse fromDbByLocation = retrieveCustomerWarehouseByCompanyAndLocation(customerWarehouse.getCompanyLocation().getId());

        if (fromDbByLocation != null) {
            throw new BadRequestException("Customer Warehouse already exist for the selected Company and Company Location");
        }

        Country country = countryRepository.findByIso(customerWarehouse.getCountry().getIso());

        if (country == null) {
            throw new ResourceNotFoundException("Country '" + customerWarehouse.getCountry().getIso() + "' - '" + customerWarehouse.getCountry().getName() + "' does not exist.");
        }

        customerWarehouse.setCountry(country);
        customerWarehouse.setEstablishment(establishmentService.save(customerWarehouse.getEstablishment()));
        customerWarehouse.getLocation().setTimezone(customerWarehouse.getCountry().getTimezone());
        customerWarehouse.setLocation(locationRepository.save(customerWarehouse.getLocation()));
        updateCustomsDetailsWithCustomsType(customerWarehouse);
        if(customerWarehouse.getCustomsDetails() != null){
            customerWarehouse.setCustomsDetails(customsDetailsRepository.save(customerWarehouse.getCustomsDetails()));
        }
        customerWarehouseRepository.save(customerWarehouse);

    }

    private void updateCustomsDetailsWithCustomsType(CustomerWarehouse customerWarehouse){
        if(customerWarehouse.getCustomsDetails() != null){
            if(customerWarehouse.getCustomsDetails().getCustomsType().equals(WarehouseCustomsType.NON_BONDED_WAREHOUSE)){
                customerWarehouse.setCustomsDetails(null);
            }else if(customerWarehouse.getCustomsDetails().getCustomsType().equals(WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION) ||
                    customerWarehouse.getCustomsDetails().getCustomsType().equals(WarehouseCustomsType.CUSTOMS_CLEARANCE_PARK)){
                customerWarehouse.getCustomsDetails().setUsedForDangerousGoods(true);
                customerWarehouse.getCustomsDetails().setUsedForTempControlledGoods(true);
            }
        }
    }

    public void editCustomerWarehouse(Long customerWarehouseId, CustomerWarehouse customerWarehouse) {

        if (customerWarehouseId == null) {
            throw new BadRequestException("Customer Warehouse is required to have Id for edit");
        }

        if (customerWarehouse.getId() == null || customerWarehouse.getId().compareTo(customerWarehouseId) != 0) {
            throw new BadRequestException("Customer Warehouse id from request url and Customer Warehouse object does not match.");
        }

        customerWarehouseValidator.validate(customerWarehouse);

        CustomerWarehouse fromDB = customerWarehouseRepository.findById(customerWarehouseId);

        if (fromDB == null) {
            throw new ResourceNotFoundException("Customer Warehouse with given id does not exist.");
        }

        if (!customerWarehouse.getCompany().equals(fromDB.getCompany())
                || !customerWarehouse.getCompanyLocation().equals(fromDB.getCompanyLocation())) {
            throw new ResourceNotFoundException("Customer warehouse Company/Company Location can not be modified");
        }

        Country country = countryRepository.findByIso(customerWarehouse.getCountry().getIso());

        if (country == null) {
            throw new ResourceNotFoundException("Country '" + customerWarehouse.getCountry().getIso() + "' - '" + customerWarehouse.getCountry().getName() + "' does not exist.");
        }

        customerWarehouse.setCountry(country);

        customerWarehouse.setEstablishment(establishmentService.save(customerWarehouse.getEstablishment()));
        customerWarehouse.setLocation(locationRepository.save(customerWarehouse.getLocation()));
        updateCustomsDetailsWithCustomsType(customerWarehouse);
        if(customerWarehouse.getCustomsDetails() != null){
            customerWarehouse.setCustomsDetails(customsDetailsRepository.save(customerWarehouse.getCustomsDetails()));
        }
        customerWarehouseRepository.save(customerWarehouse);

    }

    public Iterable<CustomerWarehouse> retrieveCustomerWarehouses() {
        return customerWarehouseRepository.findAll();
    }

    public CustomerWarehouse retrieveCustomerWarehouseById(Long id) {

        if (id == null) {
            throw new BadRequestException("Can not retrieve a Customer Warehouse without id.");
        }

        CustomerWarehouse fromDB = customerWarehouseRepository.findById(id);

        if (fromDB == null) {
            throw new ResourceNotFoundException("Customer Warehouse with given id does not exist.");
        }

        return fromDB;

    }

    public CustomerWarehouse retrieveCustomerWarehouseByCompanyAndLocation(Long companyLocationId, WarehouseCompanyType type) {
        if (companyLocationId == null) {
            throw new BadRequestException("Company Location Id is Required.");
        }
        return customerWarehouseRepository.findByCompanyLocationIdAndCompanyType(companyLocationId, type);
    }
    public CustomerWarehouse retrieveCustomerWarehouseByCompanyAndLocation(Long companyLocationId) {
    	return retrieveCustomerWarehouseByCompanyAndLocation(companyLocationId, WarehouseCompanyType.COMPANY);
    }

    public List<CustomerWarehouse> retrieveCustomerWarehouseByCompany(Long companyId) {
        if (companyId == null) {
            throw new BadRequestException("Company Id is Required.");
        }
        return customerWarehouseRepository.findByCompanyId(companyId);
    }

    public void delete(Long customerWarehouseId) {

        if (customerWarehouseId == null) {
            throw new BadRequestException("Customer Warehouse Id is Required.");
        }

        CustomerWarehouse customerWarehouseToBeDeleted = customerWarehouseRepository.findOne(customerWarehouseId);

        if (customerWarehouseToBeDeleted == null) {
            throw new ResourceNotFoundException("Customer Warehouse with given id does not exist.");
        }

        customerWarehouseToBeDeleted.setDeleted(true);

        customerWarehouseRepository.save(customerWarehouseToBeDeleted);

    }

    public List<CustomerWarehouse> listForCustomsDetails(Long customsOfficeId, WarehouseCustomsType customsType){
        CustomsOffice customsOffice = customsOfficeService.getOrThrowException(customsOfficeId);
        return customerWarehouseRepository.findByCustomsDetailsCustomsOfficeAndCustomsDetailsCustomsType(customsOffice, customsType);
    }
    public List<CustomerWarehouse> listForCompany(Long companyId){
        return customerWarehouseRepository.findByCompanyId(companyId);
    }

    public CustomerWarehouse findByIdAndHasCustomsDetails(Long id){
        return customerWarehouseRepository.findByIdAndCustomsDetailsNotNull(id);
    }

    public CustomerWarehouse findByLocationIdAndHasCustomsDetails(Long id, WarehouseCompanyType companyType){
        return customerWarehouseRepository.findByCompanyLocationIdAndCustomsDetailsNotNull(id).stream().filter(e->Objects.equal(e.getCompanyType(), companyType)).findFirst().orElse(null);
    }
    public List<CustomerWarehouse> findByCustomsDetailsCustomsType(WarehouseCustomsType customsType){
        return customerWarehouseRepository.findByCustomsDetailsCustomsType(customsType);
    }

    public List<CustomerWarehouse> search(WarehouseCompanyType companyType, WarehouseCustomsType customsType, Long customsOfficeId, String countryCode, String postalCode){
        Specifications<CustomerWarehouse> specs = null;
        if(companyType != null){
            specs = CustomerWarehouseSpecification.append(specs, CustomerWarehouseSpecification.havingCompanyType(companyType));
        }
        if(customsType != null){
            specs = CustomerWarehouseSpecification.append(specs, CustomerWarehouseSpecification.havingCustomsDetailsCustomsType(customsType));
        }
        if(customsOfficeId != null){
            specs = CustomerWarehouseSpecification.append(specs, CustomerWarehouseSpecification.havingCustomsDetailsCustomsOffice(customsOfficeId));
        }
        if(countryCode != null){
            specs = CustomerWarehouseSpecification.append(specs, CustomerWarehouseSpecification.havingAdressCountryCode(countryCode));
        }
        if(postalCode != null){
            specs = CustomerWarehouseSpecification.append(specs, CustomerWarehouseSpecification.havingAdressPostalCode(postalCode));
        }
        return customerWarehouseRepository.findAll(specs);
    }

	public Page<CustomerWarehouse> searchcustomerWarehouse(SearchWarehouseJson terms) {
    	return customerWarehouseRepository.findAll(terms.toSpecification(), new PageRequest(terms.getPage(), terms.getPageSize()));
    }
}
