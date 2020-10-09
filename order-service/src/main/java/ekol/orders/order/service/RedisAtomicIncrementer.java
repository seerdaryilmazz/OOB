package ekol.orders.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RedisAtomicIncrementer {

    @Value("${cache.prefix}")
    private String cachePrefix;

    private RedisAtomicLong orderCodeCounter;
    private RedisAtomicLong shipmentCodeCounter;

    @Autowired
    public RedisAtomicIncrementer(RedisConnectionFactory redisConnectionFactory){
        final String ORDER_CODE_KEY = cachePrefix + ":counter:order-code";
        final String SHIPMENT_CODE_KEY = cachePrefix + ":counter:shipment-code";
        this.orderCodeCounter = new RedisAtomicLong(ORDER_CODE_KEY, redisConnectionFactory);
        this.shipmentCodeCounter = new RedisAtomicLong(SHIPMENT_CODE_KEY, redisConnectionFactory);
    }

    public Long getNewOrderCode(){
        return orderCodeCounter.incrementAndGet();
    }

    public Long getNewShipmentCode(){
        return shipmentCodeCounter.incrementAndGet();
    }
}
