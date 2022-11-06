package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.itmo.highload.storoom.models.DTOs.UserFullDTO;
import static ru.itmo.highload.storoom.models.DTOs.UserReadDTO;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Page<UserReadDTO> getAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping(params = "userType")
    @PreAuthorize("hasAuthority('superuser')")
    public Page<UserReadDTO> getAllByType(@RequestParam String userType, Pageable pageable) {
        return userService.getAllByType(userType, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> create(@RequestBody UserFullDTO req) {
        UserReadDTO res = userService.create(req);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<Object> updatePassword(@PathVariable String username, @RequestBody UserFullDTO req) {
        userService.updatePassword(username, req.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/type")
    @PreAuthorize("hasAuthority('admin') and #username != authentication.name")
    public ResponseEntity<Object> updateUserType(@PathVariable String username, @RequestBody UserFullDTO req) {
        userService.updateUserType(username, req.getUserType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> deleteByUsername(Authentication auth, @PathVariable String username) {
        List<UserType> authorities = auth.getAuthorities().stream()
                .map(i -> UserType.valueOf(i.toString()))
                .collect(Collectors.toList());
        userService.deleteByUsername(username, authorities);
        return ResponseEntity.noContent().build();
    }
}
