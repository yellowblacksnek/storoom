package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.UserClient;
import static ru.itmo.highload.storromm.aggregator.utils.Utils.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> getAll(Pageable pageable) {
        return userClient.getAll(pageable);
    }

    @GetMapping(params = "userType")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> getAllByType(@RequestParam String userType, Pageable pageable) {
        return userClient.getAllByType(userType, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> create(@RequestBody Map<String, String> req) {
        return userClient.create(req);
    }

    @PutMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<Object> updatePassword(@PathVariable String username, @RequestBody Map<String, String> req) {
        return userClient.updatePassword(username, req);
    }

    @PutMapping("/{username}/type")
    @PreAuthorize("hasAuthority('admin') and #username != authentication.name")
    public ResponseEntity<Object> updateUserType(@PathVariable String username, @RequestBody Map<String, String> req) {
        return userClient.updateUserType(username, req);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> deleteByUsername(Authentication auth, @PathVariable String username) {
        return userClient.deleteByUsername(username, getHighestAuthority(auth));
    }
}
