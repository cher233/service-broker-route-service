package org.servicebroker.routeservice.config;

import com.jolbox.bonecp.BoneCPDataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class OpenJpaConfig{

    private static final String BASE_PACKAGE = "org.servicebroker.routeservice";

    @Setter
    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriverClassName;

    @Setter
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Setter
    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Setter
    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Setter
    @Value("${spring.datasource.minConnectionsPerPartition}")
    private int minConnectionsPerPartition;

    @Setter
    @Value("${spring.datasource.maxConnectionsPerPartition}")
    private int maxConnectionsPerPartition;

    @Setter
    @Value("${spring.datasource.acquireIncrement}")
    private int acquireIncrement;

    @Setter
    @Value("${spring.datasource.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;

    @Setter
    @Value("${spring.datasource.idleMaxAge}")
    private int idleMaxAge;

    @Setter
    @Value("${spring.datasource.partitionCount}")
    private int partitionCount;

    @Setter
    @Value("${spring.datasource.statementsCacheSize}")
    private int statementsCacheSize;

    /**
     * @return DataSource instance
     */
    @Bean(destroyMethod="close")
    public DataSource dataSource() {
//        log.info("***** Going to get data source from ***** ***** " + dataSourceUrl);
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(dataSourceDriverClassName);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
        dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);
        dataSource.setIdleMaxAgeInSeconds(idleMaxAge);
        dataSource.setStatementsCacheSize(statementsCacheSize);
        return dataSource;
    }

    @Bean
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        OpenJpaVendorAdapter jpaVendorAdapter = new OpenJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef =
                new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource());
        lef.setJpaVendorAdapter(createJpaVendorAdapter());
        lef.setPackagesToScan(BASE_PACKAGE);
        lef.setJpaPropertyMap(getVendorProperties());
        return lef;
    }


    public Map<String, Object> getVendorProperties() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openjpa.Log", "DefaultLevel=WARN, Tool=WARN, SQL=WARN, Runtime=WARN");
        map.put("openjpa.jdbc.Schema", "route_service" );
        map.put("openjpa.jdbc.DBDictionary", "postgres");
        map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        map.put("openjpa.RuntimeUnenhancedClasses", "supported");
        map.put("openjpa.ConnectionFactoryProperties", "PrintParameters=True");
        map.put("openjpa.DynamicEnhancementAgent", "true");
        map.put("openjpa.Multithreaded", "true" );
        map.put("openjpa.FlushBeforeQueries", "true");
        map.put("openjpa.DataCache", "false");
        map.put("openjpa.QueryCache", "false");

        return map;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}