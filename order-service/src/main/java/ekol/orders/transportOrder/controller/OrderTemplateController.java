package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.OrderTemplate;
import ekol.orders.transportOrder.service.OrderTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by burak on 09/09/16.
 */

@RestController
@RequestMapping("/order-template")
public class OrderTemplateController {

    @Autowired
    OrderTemplateService orderTemplateRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public Iterable<OrderTemplate> getOrderTemplate() {

        return orderTemplateRepository.findAll();
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public void saveOrderTemplate(@RequestBody OrderTemplate orderTemplate) {

        orderTemplateRepository.addOrderTemplate(orderTemplate);
    }

}
