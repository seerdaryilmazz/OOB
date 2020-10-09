package ekol.orders.transportOrder.controller;

import ekol.exceptions.BadRequestException;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.repository.TransportOrderRequestRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
public class CommonController { // TODO: Belli bir yere koyamadığız metodlar için böyle bir controller yaptık ama doğru mu yaptık?

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    // TODO: Bu metod, OO-1634 nolu maddedeki "herhangi bir siparişte kullanılmış olan spot teklifler listelenmemelidir"
    // ifadesine istinaden hazırlandı, ama sonuca ulaşmanın daha kolay ve şık bir yöntemi olmalı.
    @RequestMapping(value = "/was-offer-no-used", method = RequestMethod.GET)
    public boolean wasOfferNoUsed(@RequestParam String offerNo) {

        if (StringUtils.isBlank(offerNo)) {
            throw new BadRequestException("An offer no must be specified.");
        }

        boolean wasOfferNoUsed = false;
        List<TransportOrderRequest> list = transportOrderRequestRepository.findAllByOfferNo(offerNo);

        for (TransportOrderRequest request : list) {
            if (request.getOrder() != null) {
                wasOfferNoUsed = true;
                break;
            }
        }

        return wasOfferNoUsed;
    }
}
