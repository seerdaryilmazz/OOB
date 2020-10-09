package ekol.orders.lookup.controller;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.PackageGroup;
import ekol.orders.lookup.repository.PackageGroupRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/package-group")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PackageGroupController extends BaseLookupApiController<PackageGroup> {

    private PackageGroupRepository packageGroupRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(packageGroupRepository);
    }

    @Override
    @Authorize(operations="order.package-group.manage")
    public PackageGroup add(@RequestBody PackageGroup packageGroup) {

        if(StringUtils.isEmpty(packageGroup.getCode())) {
            throw new BadRequestException("Code is required.");
        }

        if(StringUtils.isEmpty(packageGroup.getName())) {
            throw new BadRequestException("Name is required.");
        }

        return super.add(packageGroup);
    }

    @Override
    @Authorize(operations="order.package-group.manage")
    public PackageGroup update(@PathVariable Long id, @RequestBody PackageGroup packageGroup) {
    	if(!packageGroupRepository.exists(id)) {
    		throw new ResourceNotFoundException("Package Group with given id '" + id + "' not found.");
    	}

        if(StringUtils.isEmpty(packageGroup.getCode())) {
            throw new BadRequestException("Code is required.");
        }

        if(StringUtils.isEmpty(packageGroup.getName())) {
            throw new BadRequestException("Name is required.");
        }

        return super.update(id, packageGroup);
    }

    @Override
    @Authorize(operations="order.package-group.manage")
    public void delete(@PathVariable Long id) {
    	if(!packageGroupRepository.exists(id)) {
    		throw new ResourceNotFoundException("Package Group with given id '" + id + "' not found.");
    	}
        super.delete(id);
    }

}
