package ekol.authorization.service;

import ekol.authorization.domain.OperationUrl;
import ekol.authorization.domain.OperationUrlMethod;
import ekol.authorization.domain.dto.OperationUrlCacheItem;
import ekol.authorization.repository.OperationUrlMethodRepository;
import ekol.authorization.repository.OperationUrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kilimci on 25/07/2017.
 */
@Service
public class OperationUrlCacheService {

    @Autowired
    private RedisTemplate stringRedisTemplate;

    @Autowired
    private OperationUrlRepository operationUrlRepository;

    @Autowired
    private OperationUrlMethodRepository operationUrlMethodRepository;

    private static final String OPERATIONS_KEY = "oneorder:operations:";
    private static final String SEPARATOR = "|";

    public void buildCacheIfEmpty(String serviceName){
        long count = stringRedisTemplate.opsForSet().size("oneorder:operations:" + serviceName);
        if(count == 0){
            buildCache(serviceName);
        }
    }
    public void refreshCache(String serviceName){
        deleteCache(serviceName);
        buildCache(serviceName);
    }

    private String key(String serviceName){
        return OPERATIONS_KEY + serviceName;
    }

    private void deleteCache(String serviceName){
        String key = key(serviceName);
        stringRedisTemplate.delete(key);
    }

    private void buildCache(String serviceName){
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        List<OperationUrl> operationUrls = operationUrlRepository.findByServiceName(serviceName);
        String key = key(serviceName);
        for(OperationUrl each: operationUrls){
            List<OperationUrlMethod> operationUrlMethods = operationUrlMethodRepository.findByOperationUrlId(each.getId());
            for(OperationUrlMethod eachMethod: operationUrlMethods){
                String operation = each.getOperation() != null ? each.getOperation().getName() : "";
                String str = StringUtils.join(Arrays.asList(each.getUrl(), eachMethod.getMethod().name(), operation), SEPARATOR);
                setOps.add(key, str);
            }
        }
    }
    
    public Set<OperationUrlCacheItem> getCache(String serviceName){
        Set<OperationUrlCacheItem> cache = new HashSet<>();
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        for (String str : setOps.members(key(serviceName))) {
            /**
             * StringUtils.split() metodu boş string'leri gözardı ettiği için StringUtils.splitPreserveAllTokens()
             * metodunu kullandık çünkü operation boş olabiliyor.
             */
            String[] parts = StringUtils.splitPreserveAllTokens(str, SEPARATOR);
            String url = parts[0];
            String method = parts[1];
            String operation = parts[2];
            cache.add(new OperationUrlCacheItem(url, method, operation));
        }
        return cache;
    }
}
