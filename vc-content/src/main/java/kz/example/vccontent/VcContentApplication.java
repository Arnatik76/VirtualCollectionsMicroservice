package kz.example.vccontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"kz.example"})
@EntityScan(basePackages = {"kz.example.entity"})
@RefreshScope
public class VcContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(VcContentApplication.class, args);
    }

}
