package stud.relamapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistry {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "serviceregistry");
        SpringApplication.run(ServiceRegistry.class, args);
    }

}
