package ekol.agreement.queue.service;

import ekol.agreement.queue.domain.dto.ChangeJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class HistoryService {


    @Value("${oneorder.crm-history-service}")
    private String historyServiceName;

    @Autowired
    private RestTemplate restTemplate;

    public List<ChangeJson> getChanges(Long id) {
        return Arrays.asList(restTemplate.getForObject(historyServiceName + "/history/changes?id={id}&type=agreement", ChangeJson[].class, id));
    }
}
