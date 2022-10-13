package ru.itmo.highload.storoom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import ru.itmo.highload.storoom.utils.TokenUtils;

import java.util.List;

@SpringBootTest()
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "ADMIN_USERNAME=admin",
        "ADMIN_PASSWORD=admin"
})
public abstract class BaseTests {

    @Autowired
    TokenUtils utils;
    public String getToken(String username, List<GrantedAuthority> authoritiesList) {
        return "Bearer " + utils.generateToken(username, authoritiesList, 10000L);
    }

    public String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
}
