package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.UserClient;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserPasswordDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserReadDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserUserTypeDTO;

import static ru.itmo.highload.storromm.aggregator.utils.Utils.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UserReadDTO> getAll(Pageable pageable) {
        return userClient.getAll(pageable);
    }

    @GetMapping(params = "userType")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UserReadDTO> getAllByType(@RequestParam String userType, Pageable pageable) {
        return userClient.getAllByType(userType, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UserReadDTO> create(@RequestBody UserFullDTO req) {
        return userClient.create(req);
    }

    @PutMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<UserReadDTO> updatePassword(@PathVariable String username, @RequestBody UserPasswordDTO req) {
        return userClient.updatePassword(username, req);
    }

    @PutMapping("/{username}/type")
    @PreAuthorize("hasAuthority('admin') and #username != authentication.name")
    public ResponseEntity<UserReadDTO> updateUserType(@PathVariable String username, @RequestBody UserUserTypeDTO req) {
        return userClient.updateUserType(username, req);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UserReadDTO> deleteByUsername(Authentication auth, @PathVariable String username) {
        return userClient.deleteByUsername(username, getHighestAuthority(auth));
    }
}
