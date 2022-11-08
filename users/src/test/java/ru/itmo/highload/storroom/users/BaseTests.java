package ru.itmo.highload.storroom.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest()
@AutoConfigureMockMvc
@Sql({"/clean.sql","/populate.sql"})
public abstract class   BaseTests {

    protected final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Value("${test.token.client}") private String clientToken;
    @Value("${test.token.admin}") private String  adminToken;
    @Value("${test.token.superuser}") private String superuserToken;

    String clientToken() {return "Bearer " + clientToken;}
    String adminToken() {return "Bearer " + adminToken;}
    String superuserToken() {return "Bearer " + superuserToken;}



    public String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
}
