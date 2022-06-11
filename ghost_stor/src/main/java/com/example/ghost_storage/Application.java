package com.example.ghost_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.ghost_storage.Controllers",
        "com.example.ghost_storage.Storage",
        "com.example.ghost_storage.Config",
        "com.example.ghost_storage.Model",
        "com.example.ghost_storage.Services"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
