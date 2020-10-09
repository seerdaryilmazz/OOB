package ekol.notification.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.notification.domain.*;
import ekol.notification.service.UserPreferenceService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user-preference")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserPreferenceController {
	private UserPreferenceService  userPreferenceService;
	
	@GetMapping
	public Set<UserPreference> my() {
		return userPreferenceService.my();
	}
	
	@PatchMapping("/{id}/status")
	public void patch(@PathVariable String id, @RequestParam Status status) {
		userPreferenceService.updateStatus(id, status);
	}

	@PatchMapping("/{id}/channel-status")
	public void patch(@PathVariable String id, @RequestParam Channel channel, @RequestParam Status status) {
		userPreferenceService.updateChannelStatus(id, channel, status);
	}
}
