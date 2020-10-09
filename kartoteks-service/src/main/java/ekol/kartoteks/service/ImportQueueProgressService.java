package ekol.kartoteks.service;

import ekol.resource.oauth2.SessionOwner;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ImportQueueProgressService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SessionOwner sessionOwner;

    @Value("${cache.prefix}")
    private String cachePrefix;

    private String buildKey(Long queueId){
        return new StringBuilder(48)
                .append(cachePrefix).append(":import-queue-in-progress:")
                .append(queueId)
                .toString();
    }

    public boolean isInProgress(Long queueId){
        return StringUtils.isNotBlank(stringRedisTemplate.opsForValue().get(buildKey(queueId)));
    }
    public String getStartedBy(Long queueId){
        return stringRedisTemplate.opsForValue().get(buildKey(queueId));
    }
    public void finishImport(Long queueId){
        stringRedisTemplate.opsForValue().getOperations().delete(buildKey(queueId));
    }
    public boolean startImport(Long queueId){
        String startedBy = getStartedBy(queueId);
        if(StringUtils.isNotBlank(startedBy) && !startedBy.equals(sessionOwner.getCurrentUser().getUsername())){
            return false;
        }
        stringRedisTemplate.opsForValue().set(buildKey(queueId), sessionOwner.getCurrentUser().getUsername(), 30, TimeUnit.MINUTES);
        return true;
    }

}
