package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class MoviesDataBaseConfig {
    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(serviceCredentials.jdbcUrl("movies-mysql"));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        moviesEntityManagerFactoryBean.setDataSource(moviesDataSource);
        moviesEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        moviesEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.movies");
        moviesEntityManagerFactoryBean.setPersistenceUnitName("movies-unit");
        return moviesEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(EntityManagerFactory moviesEntityManagerFactoryBean){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(moviesEntityManagerFactoryBean);
        return jpaTransactionManager;
    }


    @Bean
    public TransactionOperations moviesTransactionOperations(PlatformTransactionManager moviesPlatformTransactionManager){
        return new TransactionTemplate(moviesPlatformTransactionManager);
    }
}
