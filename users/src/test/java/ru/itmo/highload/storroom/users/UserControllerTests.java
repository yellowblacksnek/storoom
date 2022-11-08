package ru.itmo.highload.storroom.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storroom.users.models.UserType;
import ru.itmo.highload.storroom.users.models.UserEntity;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ru.itmo.highload.storroom.users.dtos.UserFullDTO;
import ru.itmo.highload.storroom.users.repositories.UserRepositoryImpl;


public class UserControllerTests extends BaseTests{
    @Autowired private UserRepositoryImpl repo;
    @Autowired private MockMvc mockMvc;

    @BeforeEach
    public void cleanUp() {
//        repo.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        long userCount = repo.count();

        String token = superuserToken();
        ResultActions response = mockMvc.perform(get("/users").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(userCount));
        response.andExpect(jsonPath("$.content.size()").value(userCount));
    }

    @Test
    public void testGetClients() throws Exception {
        int clientsCount = 0;
        List<UserEntity> users = repo.findAll();
        for(UserEntity u : users) {
            if(u.getUserType() == UserType.client) clientsCount++;
        }

        String token = superuserToken();
        ResultActions response = mockMvc.perform(get("/users?userType=client").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(clientsCount));
        response.andExpect(jsonPath("$.content.size()").value(clientsCount));
    }

    @Test
    public void testAddUser() throws Exception {
        long userCount = repo.count();

        UserFullDTO req = new UserFullDTO();
        req.setUsername("test");
        req.setPassword("test");
        req.setUserType(UserType.client);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isOk());

        mockMvc.perform(get("/users").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(userCount+1))
                .andExpect(content().string(containsString("test")));
    }

    @Test
    public void testAddUserAuthorization() throws Exception {
        UserFullDTO req = new UserFullDTO();
        req.setUsername("test");
        req.setPassword("test");
        req.setUserType(UserType.client);

        String token = clientToken();
        ResultActions response = mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "asd3";
        UserFullDTO req = new UserFullDTO();
        req.setPassword("new_pass");

        String token = clientToken();
        ResultActions response = mockMvc.perform(put("/users/"+username+"/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isNoContent());

        UserEntity user = repo.findByUsername("asd3");
        assertTrue(bcrypt.matches("new_pass", user.getPassword()));
    }

    @Test
    public void testUpdateUserPasswordAuthorization() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "another_name";
        UserFullDTO req = new UserFullDTO();
        req.setPassword("new_pass");

        String token = clientToken();
        ResultActions response = mockMvc.perform(put("/users/"+username+"/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserType() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "name";
        UserFullDTO req = new UserFullDTO();
        req.setUserType(UserType.superuser);

        String token = adminToken();
        ResultActions response = mockMvc.perform(put("/users/"+username+"/type")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isNoContent());
        UserEntity user = repo.findByUsername("name");
        assertEquals(UserType.superuser, user.getUserType());
    }

    @Test
    public void testUpdateUserTypeAuthorization() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "name";
        UserFullDTO req = new UserFullDTO();
        req.setUserType(UserType.superuser);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(put("/users/"+username+"/type")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));
        assertNotNull(repo.findByUsername("name"));

        String username = "name";

        String token = superuserToken();
        ResultActions response = mockMvc.perform(delete("/users/" + username )
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isNoContent());

        assertNull(repo.findByUsername("name"));
    }

    @Test
    public void testDeleteUserAuthorization() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));
        assertNotNull(repo.findByUsername("name"));

        String username = "name";

        String token = clientToken();
        ResultActions response = mockMvc.perform(delete("/users/" + username )
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isForbidden());
    }
}
