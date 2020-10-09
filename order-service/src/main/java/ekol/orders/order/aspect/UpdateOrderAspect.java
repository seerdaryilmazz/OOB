package ekol.orders.order.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.event.Operation;
import ekol.orders.order.event.OrderEvent;
import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class UpdateOrderAspect {
	
    private ApplicationEventPublisher publisher;
	
	@Pointcut("execution( public ekol.orders.order.domain.Order ekol.orders.order.service.UpdateOrder*.update*(..) )")
	public void afterUpdateOrderPointcut() {}
	
	@AfterReturning(pointcut="afterUpdateOrderPointcut()", returning="order")
	public void afterUpdateOrder(JoinPoint jointPoint, Order order ) throws Throwable {
		publisher.publishEvent(OrderEvent.with(order, Operation.UPDATE));
	}

	@Pointcut("execution( public ekol.orders.order.domain.OrderShipment ekol.orders.order.service.UpdateOrderShipment*.*(..) )")
	public void afterUpdateShipmentPointcut() {}
	
	@AfterReturning(pointcut="afterUpdateShipmentPointcut()", returning="orderShipment")
	public void afterUpdateOrder(JoinPoint jointPoint, OrderShipment orderShipment ) throws Throwable {
		publisher.publishEvent(OrderEvent.with(orderShipment.getOrder(), Operation.UPDATE));
	}
}
