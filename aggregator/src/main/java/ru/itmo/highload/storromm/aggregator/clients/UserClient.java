package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserPasswordDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserReadDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserUserTypeDTO;

import java.util.UUID;

@FeignClient(name = "USERS-SERVICE")
public interface UserClient {
    @GetMapping(value = "/users/{id}")
    ResponseEntity<UserReadDTO> getById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/users")
    ResponseEntity<UserFullDTO> getByUsername(@RequestParam(value="username") String username);

    @GetMapping(value = "/users")
    ResponseEntity<UserReadDTO> getAll(Pageable pageable);

    @GetMapping(value = "/users")
    ResponseEntity<UserReadDTO> getAllByType(@RequestParam String userType, Pageable pageable);

    @PostMapping(value = "/users")
    ResponseEntity<UserReadDTO> create(@RequestBody UserFullDTO body);

    @PutMapping(value = "/users/{username}/password")
    ResponseEntity<UserReadDTO> updatePassword(@PathVariable String username, @RequestBody UserPasswordDTO body);

    @PutMapping(value = "/users/{username}/type")
    ResponseEntity<UserReadDTO> updateUserType(@PathVariable String username, @RequestBody UserUserTypeDTO body);

    @DeleteMapping(value = "/users/{username}")
    ResponseEntity<UserReadDTO> deleteByUsername(@PathVariable String username, @RequestParam String callerAuthority);
}
