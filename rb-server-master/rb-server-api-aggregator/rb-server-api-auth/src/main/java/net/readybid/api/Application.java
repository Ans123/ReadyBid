package net.readybid.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Created by stefan on 8/30/2016.
 *
 */
@Profile(value = {"default"})
@SpringBootApplication
@ComponentScan({"net.readybid"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    public Application(Environment env) {
        System.out.println("env = " + env.getRequiredProperty("config.version"));
    }
}