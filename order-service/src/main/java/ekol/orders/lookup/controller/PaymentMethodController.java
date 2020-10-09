package ekol.orders.lookup.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/payment-method")
public class PaymentMethodController extends BaseLookupApiController<PaymentMethod> {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(paymentMethodRepository);
    }
}
