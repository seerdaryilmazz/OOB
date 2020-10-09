package ekol.authorization.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * Created by ozer on 08/03/2017.
 */
@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories("ekol.authorization.repository.auth")
public class Neo4jConfig {

    @Value("${neo4j.url}")
    private String url;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private String password;

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI(url)
                .setCredentials(username, password);
        return config;
    }
    @Bean
    public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration) {
        return new SessionFactory(configuration, "ekol.authorization.domain.auth", "BOOT-INF.classes.ekol.authorization.domain.auth");
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new ChainedTransactionManager(
                new JpaTransactionManager(entityManagerFactory),
                new Neo4jTransactionManager(sessionFactory(getConfiguration())));
    }
}
