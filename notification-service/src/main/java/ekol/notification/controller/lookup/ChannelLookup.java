package ekol.notification.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import ekol.notification.domain.Channel;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/channel")
public class ChannelLookup extends BaseEnumApiController<Channel> {

	@PostConstruct
	private void init() {
		setType(Channel.class);
	}
}
