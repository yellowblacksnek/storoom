package ru.itmo.highload.storroom.locations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@SpringBootTest()
@AutoConfigureMockMvc
public abstract class BaseTests {
    @Value("${test.token.client}") private String clientToken;
    @Value("${test.token.admin}") private String  adminToken;
    @Value("${test.token.superuser}") private String superuserToken;

    String clientToken() {return "Bearer " + clientToken;}
    String adminToken() {return "Bearer " + adminToken;}
    String superuserToken() {return "Bearer " + superuserToken;}

    @Autowired
    private ConnectionFactory connectionFactory;

    private void executeScriptBlocking(final Resource sqlScript) {
        Mono.from(connectionFactory.create())
                .flatMap(connection -> ScriptUtils.executeSqlScript(connection, sqlScript))
                .block();
    }

    @Autowired
    DatabaseClient database;

//    @BeforeEach
//    void setUp() {
//
//        Hooks.onOperatorDebug();
//
//        var statements = Arrays.asList(//
//                "DROP TABLE IF EXISTS customer;",
//                "CREATE TABLE customer ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");
//
//        statements.forEach(it -> database.sql(it) //
//                .fetch() //
//                .rowsUpdated() //
//                .as(StepVerifier::create) //
//                .expectNextCount(1) //
//                .verifyComplete());
//    }

    @BeforeEach
    private void rollOutTestData(@Value("classpath:/populate.sql") Resource script) {
        executeScriptBlocking(script);
    }
//
//    @AfterEach
//    private void cleanUpTestData(@Value("classpath:/clean.sql") Resource script) {
//        executeScriptBlocking(script);
//    }



    public String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
}
