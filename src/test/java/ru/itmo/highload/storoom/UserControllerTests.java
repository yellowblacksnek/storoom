package ru.itmo.highload.storoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static ru.itmo.highload.storoom.models.DTOs.*;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;



public class UserControllerTests extends BaseTests{

    @Autowired
    private UserRepository repo;

    @Autowired
    private MockMvc mockMvc;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @BeforeEach
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    public void testGetUsers() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));
        repo.save(new UserEntity("name1", bcrypt.encode("pass"), UserType.client));

        String token = getToken("user", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(get("/users").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(content().string(containsString("name")));
        response.andExpect(content().string(containsString("name1")));
        response.andExpect(jsonPath("$.totalElements").value(2));
        response.andExpect(jsonPath("$.content.size()").value(2));
    }

    @Test
    public void testAddUser() throws Exception {
        UserFullDTO req = new UserFullDTO();
        req.setUsername("test");
        req.setPassword("test");
        req.setUserType("client");

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isCreated());

        mockMvc.perform(get("/users").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(content().string(containsString("test")));
    }

    @Test
    public void testAddUserAuthorization() throws Exception {
        UserFullDTO req = new UserFullDTO();
        req.setUsername("test");
        req.setPassword("test");
        req.setUserType("client");

        String token = getToken("user", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("name");
        req.setPassword("new_pass");

        String token = getToken("name", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(patch("/users/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isOk());

        UserEntity user = repo.findByUsername("name");
        assertTrue(bcrypt.matches("new_pass", user.getPassword()));
    }

    @Test
    public void testUpdateUserPasswordAuthorization() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("another_name");
        req.setPassword("new_pass");

        String token = getToken("name", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(patch("/users/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserType() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("name");
        req.setUserType("superuser");

        String token = getToken("admin", getAuthorities(UserType.admin));
        ResultActions response = mockMvc.perform(patch("/users/type")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isOk());
        UserEntity user = repo.findByUsername("name");
        assertEquals(UserType.superuser, user.getUserType());
    }

    @Test
    public void testUpdateUserTypeAuthorization() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("name");
        req.setUserType("superuser");

        String token = getToken("client", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(patch("/users/type")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));
        assertNotNull(repo.findByUsername("name"));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("name");

        String token = getToken("superuser", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isNoContent());

        assertNull(repo.findByUsername("name"));
    }

    @Test
    public void testDeleteUserAuthorization() throws Exception {
        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));
        assertNotNull(repo.findByUsername("name"));

        UserFullDTO req = new UserFullDTO();
        req.setUsername("name");

        String token = getToken("client", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(delete("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }
}
