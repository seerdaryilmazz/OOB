package ekol.orders.lookup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import ekol.model.LookupValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.PackageGroup;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.repository.PackageGroupRepository;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import ekol.orders.transportOrder.service.PackageTypeRestrictionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/package-type")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PackageTypeController extends BaseLookupApiController<PackageType> {

    private PackageTypeRepository packageTypeRepository;
    private PackageGroupRepository packageGroupRepository;
    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;
    private PackageTypeRestrictionService packageTypeRestrictionService;

    @PostConstruct
    public void init(){
        setLookupRepository(packageTypeRepository);
    }

    @Override
    @GetMapping(value = {"/", ""})
    public List<LookupValueLabel> findAll() {

        List<PackageType> packageTypes = packageTypeRepository.findByOrderByRankAscNameAsc();
        List<LookupValueLabel> result = new ArrayList<>();

        for (PackageType pt : packageTypes) {
            result.add(new LookupValueLabel(pt.getId().toString(), pt.getName(), pt.getCode()));
        }

        return result;
    }


    @Override
    @Authorize(operations="order.package-type.manage")
    @PostMapping(value = {"/", ""})
    public PackageType add(@RequestBody PackageType packageType) {

        if (packageType.getId() != null) {
            throw new BadRequestException("Id should be empty.");
        }

        if (packageType.getCode() == null || packageType.getCode().trim().length() == 0) {
            throw new BadRequestException("Code cannot be empty.");
        }

        if (packageType.getName() == null || packageType.getName().trim().length() == 0) {
            throw new BadRequestException("Name cannot be empty.");
        }


        if (packageType.getPackageGroup() == null) {
            throw new BadRequestException("Package type can not be null");
        }

        PackageGroup packageGroup = packageGroupRepository.findOne(packageType.getPackageGroup().getId());

        if (packageGroup == null) {
            throw new ResourceNotFoundException("Package Group with given id '" + packageType.getPackageGroup().getId() + "' not found.");
        }
        if(packageType.getRank()==null){
            packageType.setRank(999);
        }

        packageType.setPackageGroup(packageGroup);

        return packageTypeRepository.save(packageType);

    }

    @GetMapping(value = {"/with-restriction", "/with-restriction/"})
    public List<PackageType> findAllWithRestriction() {

        List<PackageType> packageTypeList = packageTypeRepository.findByOrderByName();

        Map<Long, PackageType> packageTypeMap = new HashMap<>();

        for (PackageType pt : packageTypeList) {
            packageTypeMap.put(pt.getId(), pt);
        }

        List<PackageTypeRestriction> packageTypeRestrictionList = packageTypeRestrictionRepository.findAllByDeletedIsFalse();

        for (PackageTypeRestriction ptr : packageTypeRestrictionList) {
            packageTypeMap.get(ptr.getPackageType().getId()).setHasRestriction(ptr.hasRestriction("widthRangeInCentimeters","lengthRangeInCentimeters","heightRangeInCentimeters"));
        }

        return packageTypeList;
    }

    @GetMapping(value = "/with-groups")
    public List<PackageType> findAllWithGroups() {
        return packageTypeRepository.findByOrderByRankAscNameAsc();
    }

    @Override
    @Authorize(operations="order.package-type.manage")
    @PutMapping(value = {"/{id}/", "/{id}"})
    public PackageType update(@PathVariable Long id, @RequestBody PackageType packageType) {

        if (packageType.getId()== null) {
            throw new BadRequestException("Id cannot be empty.");
        }

        if (packageType.getCode() == null || packageType.getCode().trim().length() == 0) {
            throw new BadRequestException("Code cannot be empty.");
        }

        if (packageType.getName() == null || packageType.getName().trim().length() == 0) {
            throw new BadRequestException("Name cannot be empty.");
        }

        PackageType packageTypeFromDB = packageTypeRepository.findOne(packageType.getId());

        if(packageTypeFromDB == null) {
            throw new ResourceNotFoundException("Package Type with given id '" + packageType.getId() + "' not found.");
        }
        packageTypeFromDB.setCode(packageType.getCode());
        packageTypeFromDB.setName(packageType.getName());

        if(packageType.getPackageGroup() == null) {
            throw new BadRequestException("Package type can not be null");
        }

        PackageGroup packageGroup = packageGroupRepository.findOne(packageType.getPackageGroup().getId());

        if(packageGroup == null) {
            throw new ResourceNotFoundException("Package Group with given id '" + packageType.getPackageGroup().getId() + "' not found.");
        }
        if(packageType.getRank()==null){
            packageType.setRank(999);
        }


        packageTypeFromDB.setPackageGroup(packageGroup);

        PackageType updatedPackageType = packageTypeRepository.save(packageTypeFromDB);


        PackageTypeRestriction restriction = packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(id);

        if (restriction != null) {
            updatedPackageType.setHasRestriction(true);
        }

        return updatedPackageType;
    }

    @Override
    @Authorize(operations="order.package-type.manage")
    @DeleteMapping(value = {"/{id}/", "/{id}"})
    public void delete(@PathVariable Long id) {

        PackageTypeRestriction restriction = packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(id);

        if (restriction != null) {
            packageTypeRestrictionService.softDeleteRestriction(restriction.getId());
        }

        super.delete(id);
    }

    @GetMapping(value = {"/{id}/restrictions"})
    public PackageTypeRestriction findByPackageTypeId(@PathVariable Long id) {
        PackageType packageType = packageTypeRepository.findOne(id);
        if(packageType == null){
            throw new ResourceNotFoundException("Package type id not found");
        }
        return packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(id);
    }


    @GetMapping(value = {"/{id}/packageGroup"})
    public PackageGroup findPackageGroupByPackageTypeId(@PathVariable Long id) {

        if(id == null){
            throw new BadRequestException("Missing Parameter: Package Type Id");
        }

        PackageType packageType = packageTypeRepository.findOne(id);

        if(packageType != null) {
            return packageType.getPackageGroup();
        }

        return null;
    }


    @GetMapping(value = {"/byPackageGroupId/{id}"})
    public Iterable<PackageType> findByPackageGroupId(@PathVariable Long id) {

        if(id == null){
            throw new BadRequestException("Missing Parameter: Package Group Id");
        }

        return packageTypeRepository.findByPackageGroupId(id);
    }

}
