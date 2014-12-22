package com.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.todo.repository.TaskRepository;
import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.UpdateTaskData;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.PostConstruct;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TasksApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class TasksIntegrationTest {
    private final String USER_ID = "d23d23d-d23f34g-g43g-43-g34-g34g";

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${local.server.port}")
    private int port;

    @PostConstruct
    public void postConstruct() {
        RestAssured.config = RestAssuredConfig
                .config()
                .objectMapperConfig(objectMapperConfig()
                        .jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
                            @Override
                            public ObjectMapper create(Class aClass, String s) {
                                return objectMapper;
                            }
                        })
                        .defaultObjectMapperType(ObjectMapperType.JACKSON_2));
        RestAssured.port = port;
    }

    @Before
    public void before() {
        taskRepository.deleteAllInBatch();
    }

    @Test
    public void shouldFailAddingTaskWhenNoDataSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{}")
        .when()
                .post("/tasks")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailAddingTaskWhenUserHeaderNotSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewTaskData("description"))
        .when()
                .post("/tasks")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailAddingTaskWhenBlankDescriptionSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewTaskData("  "))
                .header("userId", USER_ID)
        .when()
                .post("/tasks")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailUpdatingTaskWhenNoDataSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{}")
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailUpdatingTaskWhenUserHeaderNotSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new UpdateTaskData("description", null))
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailUpdatingTaskWhenBlankDescriptionSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new UpdateTaskData("  ", null))
                .header("userId", USER_ID)
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailGettingTaskWhenUserHeaderNotSent() throws Exception {
        given()
                .accept(ContentType.JSON)
        .when()
                .get("/tasks/1")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailGettingTasksWhenUserHeaderNotSent() throws Exception {
        given()
                .accept(ContentType.JSON)
        .when()
                .get("/tasks")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailDeletingTaskWhenUserHeaderNotSent() throws Exception {
        given()
                .accept(ContentType.JSON)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldReturnResourceNotFoundWhenDeletingNotExistingTask() throws Exception {
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .delete("/tasks/1")
        .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldReturnResourceNotFoundWhenGettingNotExistingTask() throws Exception {
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks/1")
        .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldReturnResourceNotFoundWhenUpdatingNotExistingTask() throws Exception {
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
                .contentType(ContentType.JSON)
                .content(new UpdateTaskData("description", null))
        .when()
                .put("/tasks/1")
        .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldReturnEmptyTaskListWhenNoTaskExists() throws Exception {
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", empty());
    }

    @Test
    public void happyPath() throws Exception {
        //no tasks at the beginning
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", empty());

        DateTime beforeCreateTime = DateTime.now();

        //add task
        NewTaskData newTaskData = new NewTaskData("description");
        TaskData createdTaskData =
                given()
                        .accept(ContentType.JSON)
                        .header("userId", USER_ID)
                        .content(newTaskData)
                        .contentType(ContentType.JSON)
                .when()
                        .post("/tasks")
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(TaskData.class);

        assertThat(createdTaskData.getId(), notNullValue());
        assertThat(createdTaskData.getDescription(), equalTo(newTaskData.getDescription()));
        assertThat(createdTaskData.getCreationDate(), notNullValue());
        assertThat(createdTaskData.getCompletionDate(), nullValue());
        assertTrue(createdTaskData.getCreationDate().isAfter(beforeCreateTime));
        assertTrue(createdTaskData.getCreationDate().isBeforeNow());

        //get task
        TaskData getTaskData =
                given()
                        .accept(ContentType.JSON)
                        .header("userId", USER_ID)
                .when()
                        .get("/tasks/" + createdTaskData.getId())
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(TaskData.class);

        assertThat(getTaskData.getId(), equalTo(createdTaskData.getId()));
        assertThat(getTaskData.getDescription(), equalTo(createdTaskData.getDescription()));
        assertThat(getTaskData.getCreationDate(), equalTo(createdTaskData.getCreationDate()));
        assertThat(getTaskData.getCompletionDate(), equalTo(createdTaskData.getCompletionDate()));

        //get tasks
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1));

        //update task
        UpdateTaskData updateTaskData = new UpdateTaskData("description", true);
        TaskData updatedTaskData =
                given()
                        .accept(ContentType.JSON)
                        .header("userId", USER_ID)
                        .content(updateTaskData)
                        .contentType(ContentType.JSON)
                .when()
                        .put("/tasks/" + createdTaskData.getId())
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(TaskData.class);

        assertThat(updatedTaskData.getId(), equalTo(createdTaskData.getId()));
        assertThat(updatedTaskData.getDescription(), equalTo(createdTaskData.getDescription()));
        assertThat(updatedTaskData.getCreationDate(), equalTo(createdTaskData.getCreationDate()));
        assertThat(updatedTaskData.getCompletionDate(), notNullValue());
        assertTrue(updatedTaskData.getCompletionDate().isBeforeNow());
        assertTrue(updatedTaskData.getCompletionDate().isAfter(updatedTaskData.getCreationDate()));

        //get task
        getTaskData =
                given()
                        .accept(ContentType.JSON)
                        .header("userId", USER_ID)
                .when()
                        .get("/tasks/" + createdTaskData.getId())
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(TaskData.class);

        assertThat(getTaskData.getId(), equalTo(updatedTaskData.getId()));
        assertThat(getTaskData.getDescription(), equalTo(updatedTaskData.getDescription()));
        assertThat(getTaskData.getCreationDate(), equalTo(updatedTaskData.getCreationDate()));
        assertThat(getTaskData.getCompletionDate(), equalTo(updatedTaskData.getCompletionDate()));

        //get tasks
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1));

        //delete task
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .delete("/tasks/" + createdTaskData.getId())
        .then()
                .statusCode(204)
                .body(isEmptyString());

        //get tasks
        given()
                .accept(ContentType.JSON)
                .header("userId", USER_ID)
        .when()
                .get("/tasks")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(0));
    }
}
