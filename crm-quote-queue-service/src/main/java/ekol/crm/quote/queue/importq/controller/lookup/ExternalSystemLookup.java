package ekol.crm.quote.queue.importq.controller.lookup;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/import-queue/lookup/external-system")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ExternalSystemLookup {
	
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("unchecked")
	@GetMapping
	public List<CodeNamePair> get() {
		return ((List<String>) mongoTemplate.getCollection("ImportQuoteQueue").distinct("externalSystemName")).stream()
				.map(t -> CodeNamePair.with(t, t)).collect(Collectors.toList());
	}
}
