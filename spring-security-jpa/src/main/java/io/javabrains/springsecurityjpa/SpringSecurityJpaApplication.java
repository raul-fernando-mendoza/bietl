package io.javabrains.springsecurityjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ThingRepository.class} )
public class SpringSecurityJpaApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJpaApplication.class, args);
	}

}
