package relamapper.relationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class RelationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationServiceApplication.class, args);
	}
}
