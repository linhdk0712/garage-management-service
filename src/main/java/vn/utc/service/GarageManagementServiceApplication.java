package vn.utc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "vn.utc.service")
public class GarageManagementServiceApplication {

    public static void main(String[] args) {
        // Set the default timezone to GMT+7
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
        SpringApplication.run(GarageManagementServiceApplication.class, args);
    }

}
