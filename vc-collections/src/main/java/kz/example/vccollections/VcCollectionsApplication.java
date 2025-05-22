package kz.example.vccollections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"kz.example.vccollections.repository"})
@EntityScan(basePackages = {"kz.example.entity", "kz.example.vccollections.entity"})
@RefreshScope
@EnableFeignClients(basePackages = "kz.example.vccollections.client")
public class VcCollectionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(VcCollectionsApplication.class, args);
    }
}