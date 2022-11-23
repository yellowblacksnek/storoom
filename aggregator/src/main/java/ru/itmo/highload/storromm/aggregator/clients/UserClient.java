package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "USERS-SERVICE")
public interface UserClient {
    @GetMapping(value = "/internal/users/{id}")
    ResponseEntity<Object> getById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/internal/user")
    ResponseEntity<Object> getByUsername(@RequestParam(value="username") String username);

    @GetMapping(value = "/users")
    ResponseEntity<Object> getAll(Pageable pageable);

    @GetMapping(value = "/users")
    ResponseEntity<Object> getAllByType(@RequestParam String userType, Pageable pageable);

    @PostMapping(value = "/users")
    ResponseEntity<Object> create(@RequestBody Map<String, String> body);

    @PutMapping(value = "/users/{username}/password")
    ResponseEntity<Object> updatePassword(@PathVariable String username, @RequestBody Map<String, String> body);

    @PutMapping(value = "/users/{username}/type")
    ResponseEntity<Object> updateUserType(@PathVariable String username, @RequestBody Map<String, String> body);

    @DeleteMapping(value = "/users/{username}")
    ResponseEntity<Object> deleteByUsername(@PathVariable String username, @RequestParam String callerAuthority);
}
