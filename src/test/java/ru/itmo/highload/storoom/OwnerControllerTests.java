package ru.itmo.highload.storoom;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.LocationType;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.*;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.repositories.LocationRepo;
import ru.itmo.highload.storoom.repositories.OwnerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class OwnerControllerTests extends BaseTests{
    @Autowired private OwnerRepo repo;
    @Autowired private CompanyRepository companyRepo;
    @Autowired private LocationRepo locationRepo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        LocationEntity location = new LocationEntity();
        location.setAddress("location1");
        location.setLocationType(LocationType.stand);
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(location);
        locationRepo.save(location);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        owner.setLocations(locations);
        repo.save(owner);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/owners").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
        response.andExpect(jsonPath("$.content[0].name").value("owner"));
    }

    @Test
    public void testAdd() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        DTOs.OwnerDTO dto = new DTOs.OwnerDTO();
        dto.setName("owner1");
        dto.setCompanyId(company.getId());
        dto.setLocationIds(new ArrayList<>());

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(post("/owners")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.data.name").value("owner1"));

        assertEquals(1, repo.count());
    }

    @Test
    @Disabled
    public void testUpdate() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        LocationEntity location = new LocationEntity();
        location.setAddress("location1");
        location.setLocationType(LocationType.stand);
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(location);
        locationRepo.save(location);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        owner.setLocations(locations);
        repo.save(owner);

        OwnerEntity entity = repo.findAll().iterator().next();
        entity.setName("new name");
        entity.setLocations(new ArrayList<>());
        DTOs.OwnerReadDTO dto = Mapper.toOwnerDTO(entity);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(put("/owners/"+dto.getId())
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
        companyRepo.save(company);

        LocationEntity location = new LocationEntity();
        location.setAddress("location1");
        location.setLocationType(LocationType.stand);
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(location);
        locationRepo.save(location);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        owner.setLocations(locations);
        repo.save(owner);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/owners/"+company.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.message").value("Successfully deleted company!" + owner.getId()));


        assertEquals(0, repo.count());
    }
}
