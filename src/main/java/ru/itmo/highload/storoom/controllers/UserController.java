package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;
import ru.itmo.highload.storoom.services.UserService;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.itmo.highload.storoom.models.DTOs.UserFullDTO;
import static ru.itmo.highload.storoom.models.DTOs.UserReadDTO;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepo;

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Page<UserReadDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> res = userRepo.findAll(pageable);
        return res.map(Mapper::toUserReadDTO);
    }

    @GetMapping(params = "userType")
    @PreAuthorize("hasAuthority('superuser')")
    public Page<UserReadDTO> getUsersByType(@RequestParam String userType, Pageable pageable) {
        Page<UserEntity> res = userRepo.findAllByUserType(pageable, UserType.valueOf(userType));
        return res.map(Mapper::toUserReadDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> addUser(@RequestBody UserFullDTO req) {
        UserReadDTO res = userService.createUser(req);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<Object> updateUserPassword(@PathVariable String username, @RequestBody UserFullDTO req) {
        userService.changeUserPassword(username, req.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/type")
    @PreAuthorize("hasAuthority('admin') and #username != authentication.name")
    public ResponseEntity<Object> updateUserType(@PathVariable String username, @RequestBody UserFullDTO req) {
        userService.changeUserType(username, req.getUserType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> deleteUser(Authentication auth, @PathVariable String username) {
        List<UserType> authorities = auth.getAuthorities().stream()
                .map(i -> UserType.valueOf(i.toString()))
                .collect(Collectors.toList());
        userService.deleteUser(username, authorities);
        return ResponseEntity.noContent().build();
    }
}
