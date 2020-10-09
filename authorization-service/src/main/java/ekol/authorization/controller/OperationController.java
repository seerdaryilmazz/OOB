package ekol.authorization.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.Operation;
import ekol.authorization.dto.OperationFilterRequest;
import ekol.authorization.repository.*;
import ekol.event.auth.Authorize;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 28/02/2017.
 */
@RestController
@RequestMapping("/operation")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OperationController {

    private OperationRepository operationRepository;
    private OperationCustomRepository operationCustomRepository;
    
    @Authorize(operations="operation.manage")
    @GetMapping({"/", ""})
    public Iterable<Operation> findAll() {
        return operationRepository.findAll();
    }
    
    @Authorize(operations="operation.manage")
    @GetMapping("/search")
    public Iterable<Operation> search(@RequestParam(value="startDate",required = false) String startDate,
                                      @RequestParam(value="endDate",required = false) String endDate,
                                      @RequestParam(value="name" , required = false) String name,
                                      @RequestParam(value="description" , required = false) String description) {

        OperationFilterRequest filter =
                OperationFilterRequest.OperationFilterRequestBuilder.anOperationFilterRequest()
                        .withDescription(description)
                        .withName(name)
                        .withStartDate(startDate)
                        .withEndDate(endDate)
                        .build();

        return operationCustomRepository.search(filter);
    }

    @Authorize(operations="operation.manage")
    @PutMapping(value = {"/{name:.+}"})
    public void updateDescription(@PathVariable String name, @RequestBody Operation request) {
        Operation operation = operationRepository.findByName(name);
        if(operation == null){
            throw new ResourceNotFoundException("Operation with name {0} does not exist", name);
        }
        operation.setDescription(StringUtils.lowerCase(request.getDescription()));
        operationRepository.save(operation);
    }

}
