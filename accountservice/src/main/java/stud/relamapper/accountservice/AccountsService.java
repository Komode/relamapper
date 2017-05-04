package stud.relamapper.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableAutoConfiguration
@EnableDiscoveryClient
public class AccountsService {

    public static void main(String[] args) {
        SpringApplication.run(AccountsService.class, args);
    }

}
