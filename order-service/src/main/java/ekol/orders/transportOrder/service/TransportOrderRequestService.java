package ekol.orders.transportOrder.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.model.User;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.OrderType;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.domain.TransportOrderRequestDocument;
import ekol.orders.transportOrder.repository.TransportOrderRequestDocumentRepository;
import ekol.orders.transportOrder.repository.TransportOrderRequestRepository;
import ekol.resource.oauth2.SessionOwner;
import ekol.resource.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class TransportOrderRequestService {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Value("${oneorder.user-service}")
    private String userServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private TransportOrderService transportOrderService;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private TransportOrderRequestDocumentRepository transportOrderRequestDocumentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CreateTaskService createTaskService;

    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private ContractService contractService;

    @Autowired
    private KartoteksClient kartoteksClient;

    public TransportOrderRequest findRequestById(Long id) {
        return transportOrderRequestRepository.findOne(id);
    }

    public TransportOrderRequest findRequestWithDetailsById(Long id) {

        TransportOrderRequest request = transportOrderRequestRepository.findWithDetailsById(id);

        if (request == null) {
            throw new ResourceNotFoundException("TransportOrderRequest with specified id is not found: " + id);
        }

        if (request.getOrder() != null) {
            request.setOrder(transportOrderService.findWithDetailsById(request.getOrder().getId()));
        }

        return retrieveExternalFields(request, true, true, true);
    }

    public TransportOrderRequest findRequestWithDetailsByOrderId(Long orderId) {

        TransportOrderRequest request = transportOrderRequestRepository.findWithDetailsByOrderId(orderId);

        if (request == null) {
            throw new ResourceNotFoundException("TransportOrderRequest with specified order id is not found: " + orderId);
        }

        request.setOrder(transportOrderService.findWithDetailsById(request.getOrder().getId()));

        return retrieveExternalFields(request, true, true, true);
    }

    @Transactional
    public TransportOrderRequest createNewRequest(TransportOrderRequest request) {

        if (request == null) {
            throw new BadRequestException("A transport order request must be specified.");
        }

        if (request.getCustomerId() == null) {
            throw new BadRequestException("A customer must be specified.");
        }

        Company customer = kartoteksClient.findCompanyById(request.getCustomerId());

        if (StringUtils.isBlank(customer.getPortfolioOwner())) {
            throw new BadRequestException("Customer does not have a portfolio owner.");
        }

        if (request.getOrderType() == null) {
            throw new BadRequestException("A type (contracted or spot) must be specified.");
        }

        if (request.getOrderType().equals(OrderType.CONTRACTED)) {

            if (request.getContract() == null || request.getContract().getId() == null) {
                throw new BadRequestException("A contract must be specified.");
            }

            IdNamePair contract = contractService.findIdNamePairByIdOrThrowResourceNotFoundException(request.getContract().getId());

            request.setContract(contract);
            request.setOfferNo(null);

        } else if (request.getOrderType().equals(OrderType.SPOT)) {

            if (StringUtils.isBlank(request.getOfferNo())) {
                throw new BadRequestException("A quote number must be specified.");
            }

            request.setOfferNo(request.getOfferNo().trim().toUpperCase());
            request.setContract(null);
        }

        if (request.getReadyAtDate() == null) {
            throw new BadRequestException("A ready at date must be specified.");
        }

        if (request.getReadyAtDate().isBefore(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate())) {
            throw new BadRequestException("Ready Date must be later than current time.");
        }

        if (request.getSubsidiary() == null || request.getSubsidiary().getId() == null) {
            throw new BadRequestException("A subsidiary must be specified.");
        }

        IdNamePair subsidiary = findSubsidiaryFromCurrentUserSubsidiaries(request.getSubsidiary().getId());

        if (subsidiary == null) {
            throw new BadRequestException("Specified subsidiary must be one of the subsidiaries of current user.");
        }

        request.setSubsidiary(subsidiary);

        TransportOrderRequest persistedRequest = transportOrderRequestRepository.save(request);

        if (request.getDocuments() != null && request.getDocuments().size() > 0) {
            for (TransportOrderRequestDocument document : request.getDocuments()) {
                createDocument(persistedRequest, document);
            }
        }

        String taskId = createTaskService.createTaskForOrderEntry(persistedRequest);
        persistedRequest.setTaskId(taskId);
        transportOrderRequestRepository.save(persistedRequest);

        return persistedRequest;
    }

    @Transactional
    public TransportOrderRequest updateRequest(TransportOrderRequest request) {
        return transportOrderRequestRepository.save(request);
    }

    public TransportOrderRequest retrieveExternalFields(
            TransportOrderRequest request, boolean retrieveCustomer, boolean retrieveAcCustomer, boolean retrieveCreatedBy) {

        if (retrieveCustomer && request.getCustomerId() != null) {
            request.setCustomer(getCompany(request.getCustomerId()));
        }

        if (retrieveAcCustomer && request.getAcCustomerId() != null) {
            request.setAcCustomer(getCompany(request.getAcCustomerId()));
        }

        if (retrieveCreatedBy && request.getCreatedById() != null) {
            // TODO: id ile user sorgulama metodu user-service'ten kaldırılmış, zaten order-service tarafında da userId yerine username
            // tutulsa daha iyi olur. Şimdilik hata oluşmasını engellemek için aşağıdaki gibi yaptık.
//            request.setCreatedBy(getUser(request.getCreatedById()));
            request.setCreatedBy(null);
        }

        return request;
    }

    public List<TransportOrderRequest> retrieveExternalFields(
            List<TransportOrderRequest> list, boolean retrieveCustomer, boolean retrieveAcCustomer, boolean retrieveCreatedBy) {

        if (retrieveCustomer || retrieveAcCustomer || retrieveCreatedBy) {

            for (TransportOrderRequest request : list) {
                retrieveExternalFields(request, retrieveCustomer, retrieveAcCustomer, retrieveCreatedBy);
            }
        }

        return list;
    }

    public List<TransportOrderRequest> getAllOrderRequests(
            boolean retrieveCustomer, boolean retrieveAcCustomer, boolean retrieveCreatedBy) {

        return retrieveExternalFields(
                (List<TransportOrderRequest>) transportOrderRequestRepository.findAll(), retrieveCustomer, retrieveAcCustomer, retrieveCreatedBy);
    }

    public List<TransportOrderRequest> getLast10OrderRequestsCreatedBy(
            Long createdById, boolean retrieveCustomer, boolean retrieveAcCustomer, boolean retrieveCreatedBy) {

        return retrieveExternalFields(
                transportOrderRequestRepository.findTop10ByCreatedByIdOrderByLastUpdatedDesc(createdById),
                retrieveCustomer, retrieveAcCustomer, retrieveCreatedBy);
    }

    private TransportOrderRequestDocument createDocument(TransportOrderRequest persistedRequest, TransportOrderRequestDocument document) {

        if (document.getId() != null) {
            throw new BadRequestException("TransportOrderRequestDocument.id must be null.");
        }

        document.setRequest(persistedRequest);
        document.setDirectoryPath(fileService.getPermanentDirectory().toString());

        checkDocumentOriginalName(document);
        checkDocumentDisplayName(document);
        checkDocumentFileName(document);
        checkDocumentType(document);

        TransportOrderRequestDocument persistedDocument = transportOrderRequestDocumentRepository.save(document);

        fileService.moveFileToPermDir(document.getFileName());

        return persistedDocument;
    }

    private Company getCompany(Long companyId) {
        return oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + companyId, Company.class);
    }

    private User getUser(Long userId) {
        return oAuth2RestTemplate.getForObject(userServiceName + "/users/" + userId, User.class);
    }

    private void checkDocumentOriginalName(TransportOrderRequestDocument document) {
        if (document.getOriginalName() == null || document.getOriginalName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderRequestDocument.originalName cannot be null or empty.");
        } else {
            document.setOriginalName(document.getOriginalName().trim());
        }
    }

    private void checkDocumentDisplayName(TransportOrderRequestDocument document) {
        if (document.getDisplayName() == null || document.getDisplayName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderRequestDocument.displayName cannot be null or empty.");
        } else {
            document.setDisplayName(document.getDisplayName().trim());
        }
    }

    private void checkDocumentFileName(TransportOrderRequestDocument document) {
        if (document.getFileName() == null || document.getFileName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderRequestDocument.fileName cannot be null or empty.");
        } else {
            document.setFileName(document.getFileName().trim());
        }
    }

    private void checkDocumentType(TransportOrderRequestDocument document) {
        if (document.getType() == null || document.getType().getId() == null) {
            throw new BadRequestException("TransportOrderRequestDocument.type.id cannot be null.");
        } else {
            DocumentType type = documentTypeRepository.findOne(document.getType().getId());
            if (type == null) {
                throw new ResourceNotFoundException("TransportOrderRequestDocument with specified id cannot be found: " + document.getType().getId());
            } else {
                document.setType(type);
            }
        }
    }

    private IdNamePair findSubsidiaryFromCurrentUserSubsidiaries(Long id) {

        IdNamePair matchedSubsidiary = null;

        for (ekol.model.IdNamePair subsidiary : sessionOwner.getCurrentUser().getSubsidiaries()) {
            if (subsidiary.getId().equals(id)) {
                matchedSubsidiary = new IdNamePair(subsidiary.getId(), subsidiary.getName());
                break;
            }
        }

        return matchedSubsidiary;
    }
}
