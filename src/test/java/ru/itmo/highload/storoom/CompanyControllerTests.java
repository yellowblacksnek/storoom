package ru.itmo.highload.storoom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.*;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.repositories.OwnerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class CompanyControllerTests extends BaseTests{
    @Autowired private CompanyRepository repo;
    @Autowired private OwnerRepo ownerRepo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company");
        repo.save(company);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/companies").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
        response.andExpect(jsonPath("$.content[0].name").value("company"));
    }

    @Test
    public void testAdd() throws Exception{
        DTOs.CompanyDTO dto = new DTOs.CompanyDTO();
        dto.setName("company1");

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(post("/companies")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.data.name").value("company1"));

        assertEquals(1, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company");
        repo.save(company);

        CompanyEntity entity = repo.findAll().iterator().next();
        entity.setName("new name");
        DTOs.CompanyReadDTO dto = Mapper.toCompanyDTO(entity);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(put("/companies/"+dto.getId())
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.data.name").value("new name"));

        entity = repo.findAll().iterator().next();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        company = repo.save(company);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/companies/"+company.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.message").value("Successfully deleted company!" + company.getId()));


        assertEquals(0, repo.count());
    }
}
