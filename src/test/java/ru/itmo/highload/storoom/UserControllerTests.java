package ru.itmo.highload.storoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.itmo.highload.storoom.models.DTOs.UserFullDTO;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;



public class UserControllerTests extends BaseTests{

    @Autowired
    private UserRepository repo;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void cleanUp() {
//        repo.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        long userCount = repo.count();

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/users").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(userCount));
        response.andExpect(jsonPath("$.content.size()").value(userCount));
    }

    @Test
    public void testGetClients() throws Exception {
        int clientsCount = 0;
        List<UserEntity> users = (List<UserEntity>) repo.findAll();
        for(UserEntity u : users) {
            if(u.getUserType() == UserType.client) clientsCount++;
        }

        String token = getToken("user", getAuthorities(UserType.superuser));
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

        String token = getToken("user", getAuthorities(UserType.superuser));
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

        String token = getToken("user", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "name";
        UserFullDTO req = new UserFullDTO();
        req.setPassword("new_pass");

        String token = getToken("name", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(put("/users/"+username+"/password")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(req)));

        response.andExpect(status().isNoContent());

        UserEntity user = repo.findByUsername("name");
        assertTrue(bcrypt.matches("new_pass", user.getPassword()));
    }

    @Test
    public void testUpdateUserPasswordAuthorization() throws Exception {
//        repo.save(new UserEntity("name", bcrypt.encode("pass"), UserType.client));

        String username = "another_name";
        UserFullDTO req = new UserFullDTO();
        req.setPassword("new_pass");

        String token = getToken("name", getAuthorities(UserType.client));
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

        String token = getToken("admin", getAuthorities(UserType.admin));
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

        String token = getToken("client", getAuthorities(UserType.superuser));
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

        String token = getToken("superuser", getAuthorities(UserType.superuser));
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

        String token = getToken("client", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(delete("/users/" + username )
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isForbidden());
    }
}
