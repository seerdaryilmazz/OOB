package ekol.agreement.queue.service;

import java.time.LocalDateTime;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.transform.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.xml.transform.StringResult;

import ekol.agreement.queue.domain.RainbowQueueItem;
import ekol.agreement.queue.domain.dto.*;
import ekol.agreement.queue.enums.Status;
import ekol.agreement.queue.repository.RainbowQueueRepository;
import ekol.agreement.queue.util.Utils;
import ekol.agreement.queue.wscbfunitprice.client.RainbowClient;
import ekol.agreement.queue.wscbfunitprice.wsdl.*;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Created by Dogukan Sahinturk on 24.09.2019
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AgreementQueueService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgreementQueueService.class);

    private MongoTemplate mongoTemplate;
    private RainbowClient rainbowClient;
    private RainbowQueueRepository repository;
    private ApplicationEventPublisher publisher;

    public RainbowQueueItem findById(String id){
        return Optional.ofNullable(repository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException("Queue Item with id {0} cannot be found.", id));
    }
    
    @Transactional
    public void addQueueItem(AgreementJson agreementJson){
        WSCBFUNITPRICEInput request = rainbowClient.setRainbowRequest(agreementJson);

        RainbowQueueItem rainbowQueueItem = new RainbowQueueItem();
        rainbowQueueItem.setRawRequest(request);
        rainbowQueueItem.setRequest(Utils.stringifyXml(request));
        rainbowQueueItem.setAgreementJson(agreementJson);
        rainbowQueueItem.setRequestDate(LocalDateTime.now());
        rainbowQueueItem.setAgreementNumber(agreementJson.getNumber());
        rainbowQueueItem.setStatus(Status.PENDING);
        saveQueueItem(rainbowQueueItem);

        publisher.publishEvent(rainbowQueueItem);

    }

    public Collection<RainbowQueueItem> findByAgreementIdAndSuccessfulStatus(Long id){
        return repository.findByAgreementJson_IdAndStatusIs(id, Status.SUCCESSFUL).orElse(null);
    }

    @Transactional
    public void export(String id){
        RainbowQueueItem rainbowQueueItem = findById(id);
        if(rainbowQueueItem.getStatus() == Status.SUCCESSFUL){
            return;
        }
        try{
            WSCBFUNITPRICEOutput response = rainbowClient.sendToRainbow(rainbowQueueItem.getRawRequest());
            rainbowQueueItem.setResponse(Utils.stringifyXml(response));
            rainbowQueueItem.setResponseDate(LocalDateTime.now());
            rainbowQueueItem.setStatus("OK".equals(response.getORESULT()) ? Status.SUCCESSFUL : Status.FAILED);
            saveQueueItem(rainbowQueueItem);
        } catch (SoapFaultClientException e){
        	rainbowQueueItem.setResponse(transformSoapException(e.getSoapFault()));
            rainbowQueueItem.setResponseDate(LocalDateTime.now());
            rainbowQueueItem.setStatus(Status.FAILED);
            saveQueueItem(rainbowQueueItem);
        } catch(RuntimeException e) {
        	rainbowQueueItem.setResponseDate(LocalDateTime.now());
        	rainbowQueueItem.setStatus(Status.FAILED);
        	saveQueueItem(rainbowQueueItem);
        }
    }
    
    private String transformSoapException(SoapFault soapFault) {
    	Result l = new StringResult(); 
    	try {
    		TransformerFactory factory = TransformerFactory.newInstance();
    		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    		factory.newTransformer().transform(soapFault.getSource(), l);
    	} catch(Exception ex) {
    		LOGGER.warn("SOAP fault transform is failed", ex);
    	}
    	return l.toString();
    }

    @Transactional
    public RainbowQueueItem saveQueueItem(RainbowQueueItem queueItem){
        return repository.save(queueItem);
    }

    public Page<RainbowQueueItem> search(AgreementQueueSearchJson search){
        Pageable paging = new PageRequest(search.getPage(), search.getPageSize(),Sort.Direction.DESC, "createdAt");
        Query query = new Query().with(paging);
        if(Objects.nonNull(search.getAgreementNumber())) {
            query.addCriteria(Criteria.where("agreementNumber").is(search.getAgreementNumber()));
        }
        if(Objects.nonNull(search.getMinRequestDate()) || Objects.nonNull(search.getMaxRequestDate())) {
            List<Criteria> ranges = new ArrayList<>();
            if(Objects.nonNull(search.getMinRequestDate())) {
                ranges.add(Criteria.where("requestDate").gte(search.getMinRequestDate()));
            }
            if(Objects.nonNull(search.getMaxRequestDate())) {
                ranges.add(Criteria.where("requestDate").lte(search.getMaxRequestDate().plusDays(1)));
            }
            Criteria range = ranges.get(0);
            for (int i = 1; i < ranges.size(); i++) {
                range = range.andOperator(ranges.get(i));

            }
            query.addCriteria(range);
        }

        return PageableExecutionUtils.getPage
                (mongoTemplate.find(query, RainbowQueueItem.class), paging, ()-> mongoTemplate.count(query, RainbowQueueItem.class));
    }
}


