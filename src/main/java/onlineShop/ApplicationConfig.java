
package onlineShop;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class ApplicationConfig {

    // 最重要的sessionFactory，用来连接数据库，和初始化后mapping
    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource()); // 告诉sf MySQL 的位置or URL
        sessionFactory.setPackagesToScan("onlineShop.entity"); // scan entity 下所有class
        sessionFactory.setHibernateProperties(hibernateProperties()); // create DB 的同时船舰tables
        return sessionFactory;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        // database id：lai-project2.cxs6wcywj43l.us-east-2.rds.amazonaws.com
        // ecommerce? : 如果不存在eCommerce，就创建一个
        dataSource.setUrl("jdbc:mysql://lai-project2.cxs6wcywj43l.us-east-2.rds.amazonaws.com:3306/ecommerce?createDatabaseIfNotExist=true&serverTimezone=UTC");
        dataSource.setUsername("admin");
        dataSource.setPassword("123456pw");

        return dataSource;
    }


    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop"); // create：创建tables  drop： 项目关闭后清空数据
        // 选择使用DB 的 dialect
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect"); // MySQL5InnoDBDialect
        return hibernateProperties;
    }
}

/*
    config用来创建Hibernate的instance
    通过Hibernate 把java class map成数据库里的tables
 */