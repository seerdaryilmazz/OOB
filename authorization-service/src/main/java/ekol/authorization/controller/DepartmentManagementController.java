package ekol.authorization.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import ekol.authorization.domain.EntityStatus;
import ekol.authorization.dto.Node;
import ekol.authorization.service.DepartmentManagementService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/department-management/{departmentCode}")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class DepartmentManagementController {

	private HttpServletRequest req;
	private DepartmentManagementService customerServiceService;

	@GetMapping("/teams")
	public List<Node> listTeams(@RequestParam(required=false) String status){
		return customerServiceService.listTeamsByDepartmentCode(departmentCode(), Optional.ofNullable(status).map(EntityStatus::valueOf).orElse(null));
	}

	private String departmentCode() {
		Map<String, String> variables = (Map<String, String>)req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return variables.get("departmentCode");
	}
}
