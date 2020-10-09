package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.SalesPortfolio;
import ekol.kartoteks.repository.SalesPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 30/06/16.
 */
@RestController
@RequestMapping("/sales-portfolio")
public class SalesPortfolioController extends BaseLookupApiController<SalesPortfolio> {
    @Autowired
    private SalesPortfolioRepository salesPortfolioRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(salesPortfolioRepository);
    }
}
