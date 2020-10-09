package ekol.agreement.config;

import javax.annotation.PostConstruct;
import javax.persistence.*;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.agreement.config.listener.AgreementDeleteEventListener;

@Component
public class HibernateEventListenerConfig{
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private AgreementDeleteEventListener agreementDeleteEventListener;
	
	@PostConstruct
	private void init() {
		SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.DELETE).prependListener(agreementDeleteEventListener);
	}

}
