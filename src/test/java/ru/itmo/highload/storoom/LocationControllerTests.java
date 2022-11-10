package ru.itmo.highload.storoom;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class LocationControllerTests extends BaseTests{
    @Autowired private LocationRepo repo;

    @Autowired private OwnerRepo ownerRepo;
    @Autowired private CompanyRepository companyRepo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        ownerRepo.save(owner);
        List<OwnerEntity> owners = new ArrayList<>();
        owners.add(owner);

        LocationEntity location = new LocationEntity();
        location.setAddress("address1");
        location.setLocationType(LocationType.stand);
        location.setOwners(owners);
        repo.save(location);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/locations").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(2));
        response.andExpect(jsonPath("$.content.size()").value(2));
        response.andExpect(jsonPath("$.content[1].address").value("address1"));
    }

    @Test
    public void testAdd() throws Exception{

        DTOs.LocationDTO dto = new DTOs.LocationDTO();
        dto.setAddress("address1");
        dto.setLocationType(LocationType.stand);
        dto.setOwnerIds(new ArrayList<>());

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(post("/locations")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.data.address").value("address1"));

        assertEquals(2, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        ownerRepo.save(owner);
        List<OwnerEntity> owners = new ArrayList<>();
        owners.add(owner);

        LocationEntity entity = repo.findAll().iterator().next();
        entity.setAddress("new address");
        entity.setOwners(owners);
        DTOs.LocationReadDTO dto = Mapper.toLocationDTO(entity);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(put("/locations/"+dto.getId())
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.data.address").value("new address"));

        entity = repo.findAll().iterator().next();
        assertEquals("new address", entity.getAddress());
    }

    @Test
    public void testDelete() throws Exception{
        CompanyEntity company = new CompanyEntity();
        company.setName("company1");
        companyRepo.save(company);

        OwnerEntity owner = new OwnerEntity();
        owner.setName("owner1");
        owner.setCompany(company);
        ownerRepo.save(owner);
        List<OwnerEntity> owners = new ArrayList<>();
        owners.add(owner);

        LocationEntity location = new LocationEntity();
        location.setAddress("address1");
        location.setLocationType(LocationType.stand);
        repo.save(location);
        location.setOwners(owners);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/locations/"+location.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.message").value("Successfully deleted location! " + location.getId()));


        assertEquals(1, repo.count());
    }
}
