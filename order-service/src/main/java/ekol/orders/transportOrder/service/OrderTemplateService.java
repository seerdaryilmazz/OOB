package ekol.orders.transportOrder.service;

import ekol.exceptions.BadRequestException;
import ekol.orders.transportOrder.domain.OrderTemplate;
import ekol.orders.transportOrder.repository.OrderTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by burak on 09/09/16.
 */
@Service
public class OrderTemplateService {

    @Autowired
    OrderTemplateRepository orderTemplateRepository;

    public Iterable<OrderTemplate> findAll() {
        return orderTemplateRepository.findAll();
    }

    public void addOrderTemplate(OrderTemplate orderTemplate) {

        if(orderTemplate.getId() != null) {
            throw new BadRequestException("Order Template with 'id' can not be inserteed to database as new element!");
        }

        String code = UUID.randomUUID().toString();

        orderTemplate.setCode(code);

        //hande graph db node op.

        orderTemplateRepository.save(orderTemplate);


    }
}
