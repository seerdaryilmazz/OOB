package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.model.HangingGoodsCategory;
import ekol.crm.quote.repository.HangingGoodsCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/lookup/hanging-goods-category")
public class HangingGoodsCategoryController {

    @Autowired
    private HangingGoodsCategoryRepository repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<HangingGoodsCategory> findAll() {
        return repository.findByOrderByName();
    }
}
