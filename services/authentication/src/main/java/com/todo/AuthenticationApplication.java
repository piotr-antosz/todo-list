package com.todo;

import com.todo.service.DataLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthenticationApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthenticationApplication.class);
    }

    public static void main(String[] args) {
        AuthenticationApplication app = new AuthenticationApplication();
        SpringApplicationBuilder appBuilder = app.configure(new SpringApplicationBuilder(AuthenticationApplication.class));
        ConfigurableApplicationContext appContext = appBuilder.run(args);
        appContext.getBean(DataLoader.class).loadTestData();
    }
}
