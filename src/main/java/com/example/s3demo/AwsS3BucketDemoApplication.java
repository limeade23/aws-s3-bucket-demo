package com.example.s3demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AwsS3BucketDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsS3BucketDemoApplication.class, args);
    }

}
