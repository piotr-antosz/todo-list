package com.todo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class AuthenticationApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthenticationApplication.class);
    }

    public static void main(String[] args) {
        new AuthenticationApplication()
                .configure(new SpringApplicationBuilder(AuthenticationApplication.class))
                .run(args);
    }
}
