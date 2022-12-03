package ru.itmo.highload.storroom.locations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyDTO;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyReadDTO;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;
import ru.itmo.highload.storroom.locations.repositories.CompanyRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class CompanyTests extends BaseTests{
    @Autowired private CompanyRepository companyRepository;
    @Autowired private WebTestClient webTestClient;

    @Test
    public void testGetAll(){
        CompanyEntity entity = new CompanyEntity();
        entity.setName("company1");
        companyRepository.save(entity).block();

        List<CompanyReadDTO> companies = webTestClient.get()
                .uri("/companies")
                .exchange()
                .expectStatus().isOk()
                .returnResult(CompanyReadDTO.class)
                .getResponseBody().collectList().block();

        assertEquals(2, companies.size());

    }

    @Test
    public void testAdd() throws Exception{
        CompanyDTO dto = new CompanyDTO();
        dto.setName("company1");

        CompanyReadDTO res = webTestClient.post()
                .uri("/companies")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CompanyReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("company1", res.getName());
        assertEquals(2, companyRepository.count().block());
    }

    @Test
    public void testUpdate() throws Exception{
        CompanyEntity entity = companyRepository.findAll().blockFirst();
        CompanyReadDTO dto = Mapper.toCompanyDTO(entity);
        dto.setName("new name");

        CompanyReadDTO res = webTestClient.put()
                .uri("/companies/"+dto.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CompanyReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("new name", res.getName());

        entity = companyRepository.findAll().blockFirst();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete(){
        CompanyEntity entity = companyRepository.findAll().blockFirst();

        CompanyReadDTO res = webTestClient.delete()
                .uri("/companies/"+entity.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CompanyReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("company", res.getName());
        assertEquals(0, companyRepository.count().block());
    }
}
