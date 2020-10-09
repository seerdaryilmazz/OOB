package ekol.orders.lookup.controller;

import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrClassDetailsVersion;
import ekol.orders.lookup.repository.AdrClassDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adr-class-details")
public class AdrClassDetailsController {

    @Autowired
    private AdrClassDetailsRepository adrClassDetailsRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public Iterable<AdrClassDetails> getAllAdrClasses(){
        return  adrClassDetailsRepository.findByAdrVersion(AdrClassDetailsVersion.V2017);
    }

    @RequestMapping(value={"/{unNumber}"},method= RequestMethod.GET)
    public List<AdrClassDetails> findAdrClassDetailsByUnNumber(@PathVariable String unNumber){
        return adrClassDetailsRepository.findByAdrVersionAndUnNumber(AdrClassDetailsVersion.V2017, unNumber);
    }

    @RequestMapping(value={"/list/{ids}"},method= RequestMethod.GET)
    public List<AdrClassDetails> findAdrClassDetailsById(@PathVariable List<Long> ids){
        return adrClassDetailsRepository.findByIdIn(ids);
    }

}
