package ekol.location.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.location.trailerPark.TrailerPark;
import ekol.location.repository.TrailerParkRepository;
import ekol.location.service.TrailerParkService;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 02/05/2017.
 */
@RestController
@RequestMapping("/location/trailer-park")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class TrailerParkController {
    
	private TrailerParkService trailerParkService;
    private TrailerParkRepository trailerParkRepository;

    @Authorize(operations="location.trailer-park.manage")
    @PostMapping(value = "")
    public TrailerPark save(@RequestBody TrailerPark trailerPark) {
        return trailerParkService.save(trailerPark);
    }

    @Authorize(operations="location.trailer-park.manage")
    @PutMapping(value = "/{id}")
    public TrailerPark update(@PathVariable Long id, @RequestBody TrailerPark trailerPark) {
        check(id);
        return trailerParkService.save(trailerPark);
    }

    @GetMapping(value = "")
    public Iterable<TrailerPark> list() {
        return trailerParkRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public TrailerPark get(@PathVariable Long id) {
        check(id);
        return trailerParkRepository.findOne(id);
    }

    @Authorize(operations="location.trailer-park.manage")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        check(id);
        trailerParkService.delete(id);
    }

    private void check(Long id){
        if(!trailerParkRepository.exists(id)){
            throw new ResourceNotFoundException("Trailer park with id {0} does not exist", id);
        }
    }
}
