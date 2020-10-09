package ekol.kartoteks.controller.lookup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.kartoteks.domain.BusinessSegmentType;
import ekol.kartoteks.service.BusinessSegmentTypeService;
import lombok.AllArgsConstructor;

/**
 * Created by fatmaozyildirim on 5/5/16.
 */
@RestController
@RequestMapping("/business-segment-type")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessSegmentTypeController  {
	
    @Autowired
    private BusinessSegmentTypeService businessSegmentTypeService;

    @GetMapping
    public List<BusinessSegmentType> list() {
    	return businessSegmentTypeService.findAll();
    }

    @GetMapping(value = {"/code/{serviceAreaCode}", "/code/{serviceAreaCode}"})
    public BusinessSegmentType getByCode(@PathVariable String serviceAreaCode) {
    	return businessSegmentTypeService.findByCode(serviceAreaCode);
    }

}
