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
import ekol.location.domain.location.port.Port;
import ekol.location.repository.PortRepository;
import ekol.location.service.PortService;
import lombok.AllArgsConstructor;

/**
 * Created by burak on 11/04/17.
 */
@RestController
@RequestMapping("/location/port")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PortController {

    private PortService portService;
    private PortRepository portRepository;

    @Authorize(operations="location.port.manage")
    @PostMapping(value = "")
    public Port save(@RequestBody Port port) {
        return portService.save(port);
    }
    
    @Authorize(operations="location.port.manage")
    @PutMapping(value = "/{id}")
    public Port update(@PathVariable Long id, @RequestBody Port port) {
        check(id);
        return portService.save(port);
    }

    @GetMapping(value = "")
    public Iterable<Port> list() {
        return portRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Port get(@PathVariable Long id) {
        check(id);
        return portRepository.findOne(id);
    }

    @Authorize(operations="location.port.manage")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        check(id);
        portService.delete(id);
    }

    private void check(Long id){
        if(!portRepository.exists(id)){
            throw new ResourceNotFoundException("Port with id {0} does not exist", id);
        }
    }
}

