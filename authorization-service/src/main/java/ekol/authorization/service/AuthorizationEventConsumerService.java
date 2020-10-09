package ekol.authorization.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import ekol.authorization.domain.Operation;
import ekol.authorization.domain.OperationUrl;
import ekol.authorization.domain.OperationUrlMethod;
import ekol.authorization.repository.OperationCustomRepository;
import ekol.authorization.repository.OperationRepository;
import ekol.authorization.repository.OperationUrlMethodRepository;
import ekol.authorization.repository.OperationUrlRepository;
import ekol.event.auth.AuthorizationEvent;
import ekol.event.auth.AuthorizationEvent.AuthorizationEventItem;
import ekol.exceptions.BadRequestException;

/**
 * Created by ozer on 27/02/2017.
 */
@Service
public class AuthorizationEventConsumerService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationEventConsumerService.class);

    public static class Summary {
        private int operationInsert = 0;
        private int operationDelete = 0;
        private int operationUrlInsert = 0;
        private int operationUrlDelete = 0;
        private int operationUrlMethodInsert = 0;
        private int operationUrlMethodDelete = 0;

        public void append(Summary other){
            operationInsert += other.getOperationInsert();
            operationDelete += other.getOperationDelete();
            operationUrlDelete += other.getOperationUrlDelete();
            operationUrlMethodInsert += other.getOperationUrlMethodInsert();
            operationUrlMethodDelete += other.getOperationUrlMethodDelete();
        }

        public void incrementOperationInsert() {
            operationInsert++;
        }

        public void incrementOperationDelete() {
            operationDelete++;
        }

        public void incrementOperationUrlInsert() {
            operationUrlInsert++;
        }

        public void incrementOperationUrlDelete() {
            operationUrlDelete++;
        }

        public void incrementOperationUrlMethodInsert() {
            operationUrlMethodInsert++;
        }

        public void incrementOperationUrlMethodDelete() {
            operationUrlMethodDelete++;
        }

        public int getOperationInsert() {
            return operationInsert;
        }

        public int getOperationDelete() {
            return operationDelete;
        }

        public int getOperationUrlInsert() {
            return operationUrlInsert;
        }

        public int getOperationUrlDelete() {
            return operationUrlDelete;
        }

        public int getOperationUrlMethodInsert() {
            return operationUrlMethodInsert;
        }

        public int getOperationUrlMethodDelete() {
            return operationUrlMethodDelete;
        }
    }

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationUrlRepository operationUrlRepository;

    @Autowired
    private OperationUrlMethodRepository operationUrlMethodRepository;

    @Autowired
    private OperationCustomRepository operationCustomRepository;

    @Autowired
    private OperationUrlCacheService operationUrlCacheService;


    private void checkAuthorizationEvent(AuthorizationEvent authorizationEvent){
        if (StringUtils.isBlank(authorizationEvent.getServiceName())) {
            throw new BadRequestException("Service name can not be empty");
        }
    }

    private void checkAuthorizationEventItem(AuthorizationEvent.AuthorizationEventItem authorizationEventItem){
        if (authorizationEventItem == null
                || authorizationEventItem.getPatterns() == null || authorizationEventItem.getPatterns().isEmpty()){
            throw new BadRequestException("Patterns and operations can not be blank");
        }

        authorizationEventItem.getPatterns().forEach(pattern -> {
            if (StringUtils.isBlank(pattern)) {
                throw new BadRequestException("Pattern can not be blank");
            }
        });
    }

    private Summary addOperation(AuthorizationEvent.AuthorizationEventItem authorizationEventItem, String serviceName, String pattern, String operationName){
        Summary summary = new Summary();
        Operation persistedOperation = operationRepository.save(new Operation(operationName));
        summary.incrementOperationInsert();

        OperationUrl persistedOperationUrl = operationUrlRepository.save(new OperationUrl(persistedOperation, serviceName, pattern));
        summary.incrementOperationUrlInsert();

        authorizationEventItem.getMethods().forEach(method -> {
            operationUrlMethodRepository.save(new OperationUrlMethod(persistedOperationUrl, method));
            summary.incrementOperationUrlMethodInsert();
        });
        return summary;
    }
    private Summary addOperationUrl(AuthorizationEvent.AuthorizationEventItem authorizationEventItem, Operation operation, String serviceName, String pattern){
        // Add operation url and operation url methods
        Summary summary = new Summary();
        OperationUrl persistedOperationUrl = operationUrlRepository.save(new OperationUrl(operation, serviceName, pattern));
        summary.incrementOperationUrlInsert();

        authorizationEventItem.getMethods().forEach(method -> {
            operationUrlMethodRepository.save(new OperationUrlMethod(persistedOperationUrl, method));
            summary.incrementOperationUrlMethodInsert();
        });
        return summary;
    }
    private Summary addOperationUrlMethod(OperationUrl operationUrl, RequestMethod method){
        Summary summary = new Summary();
        OperationUrlMethod operationUrlMethod = operationUrlMethodRepository.findByOperationUrlIdAndMethod(operationUrl.getId(), method);
        if (operationUrlMethod == null) {
            // Add operation url method
            operationUrlMethodRepository.save(new OperationUrlMethod(operationUrl, method));
            summary.incrementOperationUrlMethodInsert();
        }
        return summary;
    }

    private Summary addOperationTree(AuthorizationEvent.AuthorizationEventItem authorizationEventItem, String serviceName, String pattern){
        Summary summary = new Summary();
        if(authorizationEventItem.getOperations() == null || authorizationEventItem.getOperations().isEmpty()){
            OperationUrl operationUrl = operationUrlRepository.findByOperationIsNullAndServiceNameAndUrl(serviceName, pattern);
            if (operationUrl == null) {
                summary.append(
                        addOperationUrl(authorizationEventItem, null, serviceName, pattern)
                );
            } else {
                authorizationEventItem.getMethods().forEach(method -> {
                    OperationUrlMethod urlMethod = operationUrlMethodRepository.findByOperationUrlIdAndMethod(operationUrl.getId(), method);
                    if(urlMethod == null){
                        summary.append(
                                addOperationUrlMethod(operationUrl, method)
                        );
                    }
                });
            }
        }else{
            authorizationEventItem.getOperations().forEach(operationName -> {
                Operation operation = operationRepository.findByName(operationName);
                if (operation == null) {
                    summary.append(
                            addOperation(authorizationEventItem, serviceName, pattern, operationName)
                    );
                } else {
                    OperationUrl operationUrl = operationUrlRepository.findByOperationIdAndUrl(operation.getId(), pattern);
                    if (operationUrl == null) {
                        summary.append(
                                addOperationUrl(authorizationEventItem, operation, serviceName, pattern)
                        );
                    } else {
                        authorizationEventItem.getMethods().forEach(method -> {
                            summary.append(
                                    addOperationUrlMethod(operationUrl, method)
                            );
                        });
                    }
                }
            });
        }
        return summary;
    }
    
    private Summary deleteUnusedOperations(AuthorizationEvent authorizationEvent) {
    	Summary summary = new Summary();
    	List<OperationUrl> operationUrls = Optional.ofNullable(operationUrlRepository.findByServiceName(authorizationEvent.getServiceName())).orElse(Arrays.asList());
    	for (OperationUrl operationUrl : operationUrls) {
    		List<OperationUrlMethod> operationUrlMethods =  operationUrlMethodRepository.findByOperationUrlId(operationUrl.getId());
    		Supplier<Stream<AuthorizationEventItem>> authorizationEventItemSupplier = () -> authorizationEvent.getAuthorizationEventItems().parallelStream().filter(item->item.getPatterns().contains(operationUrl.getUrl()));
    		if(Objects.isNull(operationUrl.getOperation()) || authorizationEventItemSupplier.get().flatMap(t->t.getOperations().parallelStream()).noneMatch(t->Objects.equals(t, operationUrl.getOperation().getName()))) {
    			operationUrl.setDeleted(true);
    			summary.incrementOperationUrlDelete();

    			operationUrlMethods.forEach(operationUrlMethod -> {
    				operationUrlMethod.setDeleted(true);
    				summary.incrementOperationUrlMethodDelete();
    			});

    			operationUrlRepository.save(operationUrl);
    			operationUrlMethodRepository.save(operationUrlMethods);
    		} else {
    			for (OperationUrlMethod operationUrlMethod : operationUrlMethods) {
    				if(authorizationEventItemSupplier.get().flatMap(t->t.getMethods().parallelStream()).noneMatch(t->Objects.equals(t, operationUrlMethod.getMethod()))) {
    					operationUrlMethod.setDeleted(true);
    					summary.incrementOperationUrlMethodDelete();
    					operationUrlMethodRepository.save(operationUrlMethod);
    				}
    			}
    		}
    	}
    	return summary;
    }

    @Transactional
    public void consumeAuthorizationEvent(AuthorizationEvent authorizationEvent) {

        Summary summary = new Summary();
        checkAuthorizationEvent(authorizationEvent);

        // Insert new ones
        authorizationEvent.getAuthorizationEventItems().forEach(authorizationEventItem -> {
            checkAuthorizationEventItem(authorizationEventItem);
            authorizationEventItem.getPatterns().forEach(pattern -> {
                Summary result = addOperationTree(authorizationEventItem, authorizationEvent.getServiceName(), pattern);
                summary.append(result);
            });
        });

        // Delete unnecessary ones
        deleteUnusedOperations(authorizationEvent);

        // Delete unused operations
        List<Operation> unusedOperations = operationCustomRepository.findUnusedOperations();
        if (unusedOperations != null && !unusedOperations.isEmpty()) {
            unusedOperations.forEach(unusedOperation -> {
                unusedOperation.setDeleted(true);
                operationRepository.save(unusedOperation);
                summary.incrementOperationDelete();
            });
        }

        operationUrlCacheService.refreshCache(authorizationEvent.getServiceName());

    }
}
