package ekol.hibernate5.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by kilimci on 17/04/16.
 */
@Configuration
@ConfigurationProperties(prefix = "oneorder.datasource")
public class DatasourceConfig extends HikariConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws SQLException {
        return new HikariDataSource(this);
    }

}
