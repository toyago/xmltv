package org.technicode.xmltv.conf;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@ComponentScan({"org.technicode.xmltv.core", "org.technicode.xmltv.main"})
@EnableJpaRepositories("org.technicode.xmltv.dao")
@EnableTransactionManagement
public class AppConfiguration {
	
	@Bean(name = "dataSource")
	public ComboPooledDataSource dataSource() throws PropertyVetoException{
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		//dataSource.close(true);
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/techcode_xmltv");
		dataSource.setUser("techcode_xmltv");
		dataSource.setPassword("xmltv");
		return dataSource;
	}
	
	@Bean(name = "jpaVendorAdapter")
	public HibernateJpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter(); 
		jpaVendorAdapter.setDatabase(Database.MYSQL);
		jpaVendorAdapter.setDatabasePlatform("MYSQL");
		jpaVendorAdapter.setGenerateDdl(true);
		return jpaVendorAdapter;
	}
	
	public Map<String, String> propertiesMap() {
		Map<String, String> propertiesMap = new HashMap<String, String>();
		propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		propertiesMap.put("hibernate.hbm2ddl.auto", "update");
		propertiesMap.put("hibernate.connection.charSet", "UTF-8");
		//propertiesMap.put("hibernate.show_sql", "true");
		//propertiesMap.put("hibernate.use_sql_comments", "true");
		propertiesMap.put("hibernate.enable_lazy_load_no_trans", "true");
		return propertiesMap;
	}
	
	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory() throws PropertyVetoException{
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory.setPackagesToScan("org.technicode.xmltv.model");
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPersistenceUnitName("puSearch");
		entityManagerFactory.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
		entityManagerFactory.setJpaPropertyMap(propertiesMap());
		entityManagerFactory.afterPropertiesSet();
		return entityManagerFactory.getObject();
	}
	
	@Bean(name = "transactionManager")
	public JpaTransactionManager transactionManager() throws PropertyVetoException{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}

}
