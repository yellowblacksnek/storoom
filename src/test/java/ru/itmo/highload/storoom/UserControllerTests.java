package ru.itmo.highload.storoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.hamcrest.CoreMatchers;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "ADMIN_USERNAME=admin",
        "ADMIN_PASSWORD=admin"
})
public class UserControllerTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private MockMvc mockMvc;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    @WithUserDetails("client")
    public void test() throws Exception {
        repo.save(new UserEntity("name", encoder.encode("pass"), UserType.client));
        repo.save(new UserEntity("name1", encoder.encode("pass"), UserType.client));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users"));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.content().string(containsString("name")));
        response.andExpect(MockMvcResultMatchers.content().string(containsString("name1")));
    }
}
