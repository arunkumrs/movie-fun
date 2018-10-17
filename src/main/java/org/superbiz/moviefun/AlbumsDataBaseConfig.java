package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class AlbumsDataBaseConfig {

    @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(serviceCredentials.jdbcUrl("albums-mysql"));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsEntityManagerFactoryBean(DataSource albumsDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean albumsEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        albumsEntityManagerFactoryBean.setDataSource(albumsDataSource);
        albumsEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        albumsEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.albums");
        albumsEntityManagerFactoryBean.setPersistenceUnitName("albums-unit");
        return albumsEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(EntityManagerFactory albumsEntityManagerFactoryBean){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(albumsEntityManagerFactoryBean);
        return jpaTransactionManager;
    }

    @Bean
    public TransactionOperations albumsTransactionOperations(PlatformTransactionManager albumsPlatformTransactionManager){
        return new TransactionTemplate(albumsPlatformTransactionManager);
    }

}
