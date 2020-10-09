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
import ekol.location.domain.location.terminal.Terminal;
import ekol.location.repository.TerminalRepository;
import ekol.location.service.TerminalService;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 28/04/2017.
 */
@RestController
@RequestMapping("/location/terminal")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class TerminalController {

    private TerminalService terminalService;
    private TerminalRepository terminalRepository;

    @Authorize(operations="location.terminal.manage")
    @PostMapping(value = "")
    public Terminal save(@RequestBody Terminal terminal) {
        return terminalService.save(terminal);
    }

    @Authorize(operations="location.terminal.manage")
    @PutMapping(value = "/{id}")
    public Terminal update(@PathVariable Long id, @RequestBody Terminal terminal) {
        check(id);
        return terminalService.save(terminal);
    }

    @GetMapping(value = "")
    public Iterable<Terminal> list() {
        return terminalRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Terminal get(@PathVariable Long id) {
        check(id);
        return terminalRepository.findOne(id);
    }

    @Authorize(operations="location.terminal.manage")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        check(id);
        terminalService.delete(id);
    }

    private void check(Long id){
        if(!terminalRepository.exists(id)){
            throw new ResourceNotFoundException("Terminal with id {0} does not exist", id);
        }
    }

}
