package ekol.authorization.controller;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.authorization.domain.Department;
import ekol.authorization.repository.DepartmentRepository;
import ekol.authorization.service.DepartmentService;
import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.hibernate5.domain.controller.BaseLookupApiController;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/department")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class DepartmentController extends BaseLookupApiController<Department> {

	private DepartmentRepository departmentRepository;
	private DepartmentService departmentService;

    @PostConstruct
    public void init(){
        setLookupRepository(departmentRepository);
    }
    
    @GetMapping("/by-code/{code}")
    public Department getByCode(@PathVariable String code) {
    	return departmentRepository.findByCode(code);
    }
    
    @Override
    @Authorize(operations = "department.manage")
    public Department add(@RequestBody Department lookup) {
    	return super.add(lookup);
    }
    
    @Override
    @Authorize(operations = "department.manage")
    public Department update(@PathVariable Long id, @RequestBody Department updatedLookup) {
    	return super.update(id, updatedLookup);
    }
    
    @Override
    @Authorize(operations = "department.manage")
    public void delete(@PathVariable Long id) {
    	super.delete(id);
    }

    @GetMapping(value = "/{id}/homepage")
    public String getDepartmentHomepage(@PathVariable Long id) {
        Department department = this.departmentRepository.findById(id).orElse(new Department());
        return department.getHomepage();
    }
    
    @GetMapping("/by-inheritedTeam")
    public Iterable<Department> listByInheritedTeam(@RequestParam(required=false) Long teamId, @RequestParam(required=false) Long teamNodeId){
    	if(Objects.nonNull(teamId)) {
    		return departmentService.findByInheritTeamExternalId(teamId);
    	} else if(Objects.nonNull(teamNodeId)) {
    		return departmentService.findByInheritTeamId(teamNodeId);
    	} else {
    		throw new BadRequestException("one of teamId or teamNodeId must be sent");
    	}
    }
}
