package com.sktechx.palab.logx.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@SuppressWarnings("ALL")
@Configuration
@EntityScan(basePackages={"com.sktechx.palab.logx.secondary.domain"})
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackages = {
                "com.sktechx.palab.logx.secondary.domain"
        }
)
public class SecondaryDataSourceConfiguration extends DataSourceConfiguration {

    @Value("${spring.secondaryDatasource.username}")
    private String username;

    @Value("${spring.secondaryDatasource.password}")
    private String password;

    @Value("${spring.secondaryDatasource.url}")
    private String url;

    @Autowired
    private PoolProperties poolProperties;

    @Bean
    public DataSource secondaryDataSource() {
        DataSource dataSource = new DataSource(poolProperties);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setUrl(url);
        return dataSource;
    }



    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        DataSource dataSource = secondaryDataSource();
        return builder
                .dataSource(dataSource)
                .packages("com.sktechx.palab.logx.secondary.domain")
                .persistenceUnit("apix")
                .properties(createProperties())
                .build();
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(secondaryEntityManagerFactory(builder).getObject());
    }
}