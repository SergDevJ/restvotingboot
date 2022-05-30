package ru.ssk.restvoting.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"ru.ssk.restvoting.model",
        "ru.ssk.restvoting.repository",
        "ru.ssk.restvoting.service",
        "ru.ssk.restvoting.util"})
@EnableJpaRepositories(basePackages = {"ru.ssk.restvoting.repository"})

public class DataJpaConfig {
    @Autowired
    private Environment env;

    @Bean
    public javax.sql.DataSource hsqldbDataSource() {
        DriverManagerDataSource dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource();
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new ClassPathResource("db/hsqldb.properties").getFile()));
        } catch (IOException e) {
            throw new IllegalStateException("Error creating HSQLDB datasource", e);
        }
        dataSource.setDriverClassName(props.getProperty("database.driverClassName"));
        dataSource.setUrl(props.getProperty("database.url"));
        dataSource.setUsername(props.getProperty("database.username"));
        dataSource.setPassword(props.getProperty("database.password"));

        // schema init
        //https://stackoverflow.com/questions/38040572/spring-boot-loading-initial-data/38047021#38047021
        Resource initSchema = new ClassPathResource("db/initDB_hsql.sql");
        Resource initData = new ClassPathResource("db/populateDB.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
        databasePopulator.setSqlScriptEncoding("UTF-8");
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        hibernateProp.put("hibernate.max_fetch_depth", 3);
        hibernateProp.put("hibernate.jpa.compliance.proxy", false);
        hibernateProp.put("hibernate.jdbc.batch_size", 10);
        hibernateProp.put("hibernate.jdbc.fetch_size", 50);

        hibernateProp.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        hibernateProp.put("org.ehcache.jsr107.EhcacheCachingProvider", "org.ehcache.jsr107.EhcacheCachingProvider");
        if (activeProfiles.contains("test")) {
            hibernateProp.put("hibernate.cache.use_second_level_cache", "false");
            hibernateProp.put("hibernate.cache.use_query_cache", "false");
        } else {
            hibernateProp.put("hibernate.cache.use_second_level_cache", "true");
            hibernateProp.put("hibernate.cache.use_query_cache", "true");
        }
        hibernateProp.put("javax.persistence.validation.group.pre-persist", "ru.ssk.restvoting.util.validation.ValidationGroup$Persist");
        hibernateProp.put("javax.persistence.validation.group.pre-update", "ru.ssk.restvoting.util.validation.ValidationGroup$Persist");
        return hibernateProp;
    }

    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(javax.sql.DataSource dataSource,
            Properties hibernateProperties, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ru.ssk.restvoting.model");
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaProperties(hibernateProperties);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

}
