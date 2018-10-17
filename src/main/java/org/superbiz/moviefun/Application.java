package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }


    @Bean
    public DatabaseServiceCredentials databaseServiceCredentials(@Value("${VCAP_SERVICES}") String vcapServicesJson){
        return new DatabaseServiceCredentials(vcapServicesJson);
    }

  /*  @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource albumsDataSource = new MysqlDataSource();
        albumsDataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql"));
        return albumsDataSource;
    }

    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource moviesDataSource = new MysqlDataSource();
        moviesDataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        return moviesDataSource;
    }*/

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(){
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

   /* @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean albumsEntityManagerFactoryBean(DataSource albumsDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean albumsEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        albumsEntityManagerFactoryBean.setDataSource(albumsDataSource);
        albumsEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        albumsEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.albums");
        albumsEntityManagerFactoryBean.setPersistenceUnitName("albums-unit");
        return albumsEntityManagerFactoryBean;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        moviesEntityManagerFactoryBean.setDataSource(moviesDataSource);
        moviesEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        moviesEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.movies");
        moviesEntityManagerFactoryBean.setPersistenceUnitName("movies-unit");
        return moviesEntityManagerFactoryBean;
    }*/
}
