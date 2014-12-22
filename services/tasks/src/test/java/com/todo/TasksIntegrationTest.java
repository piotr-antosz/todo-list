package com.todo;

import com.jayway.restassured.RestAssured;
import com.todo.repository.TaskRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.PostConstruct;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TasksApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class TasksIntegrationTest {
    @Autowired
    private TaskRepository taskRepository;

    @Value("${local.server.port}")
    private int port;

    @PostConstruct
    public void postConstruct() {
        RestAssured.port = port;
    }

    @Before
    public void before() {
        taskRepository.deleteAllInBatch();
    }
}
