package kz.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"kz.example.repository"})
@RefreshScope
@EnableFeignClients
public class VcUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(VcUsersApplication.class, args);
    }
}