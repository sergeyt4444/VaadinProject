package com.project;

import com.project.controller.AdminControllerInterface;
import com.project.controller.ModeratorControllerInterface;
import com.project.controller.UserControllerInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {UserControllerInterface.class,
        AdminControllerInterface.class,
        ModeratorControllerInterface.class},
        basePackageClasses= {UserControllerInterface.class,
        AdminControllerInterface.class,
        ModeratorControllerInterface.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
