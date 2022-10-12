package ru.itmo.highload.storoom.controllers;

import static ru.itmo.highload.storoom.models.DTOs.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;
import ru.itmo.highload.storoom.services.Mapper;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping
    public Page<UserReadDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> res = userRepo.findAll(pageable);
        return res.map(Mapper::toUserReadDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity addUser(@RequestBody UserFullDTO req) {
        try {
            if(userRepo.existsByUsername(req.getUsername()) || req.getUsername().equals(adminUsername)) {
                return ResponseEntity.badRequest().body("username already exists");
            }

            req.setPassword(encoder.encode(req.getPassword()));
            req.setUserType("client");

            userRepo.save(Mapper.toUserEntity(req));

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("incorrect user type");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/password")
    @PreAuthorize("#req.username == authentication.name")
    public ResponseEntity updateUserPassword(@RequestBody UserFullDTO req) {
        if (req.getUsername().equals(adminUsername)) {
            return ResponseEntity.badRequest().body("cant mess with the admin");
        }

        if (req.password == null || req.password.isEmpty()) {
            return ResponseEntity.badRequest().body("no password provided");
        }

        UserEntity user = userRepo.findByUsername(req.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("username not found");
        }
        user.setPassword(encoder.encode(req.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/type")
    @PreAuthorize("hasAuthority('admin') and #req.username != authentication.name")
    public ResponseEntity updateUserType(@RequestBody UserFullDTO req) {
        if (req.getUsername().equals(adminUsername)) {
            return ResponseEntity.badRequest().body("cant mess with the admin");
        }

        if (req.userType == null || req.userType.isEmpty()) {
            return ResponseEntity.badRequest().body("no type provided");
        }

        UserType type;
        try {
            type = UserType.valueOf(req.getUserType());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("incorrect type provided");
        }

        UserEntity user = userRepo.findByUsername(req.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("username not found");
        }
        user.setUserType(type);
        userRepo.save(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity deleteUser(Authentication auth, @RequestBody UserFullDTO req) {
        if (req.getUsername().equals(adminUsername)) {
            return ResponseEntity.badRequest().body("cant mess with the admin");
        }

        UserEntity user = userRepo.findByUsername(req.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("username not found");
        }

        UserType highestType = UserType.getHighestOf(
                auth.getAuthorities().stream()
                        .map(i -> UserType.valueOf(i.toString()))
                        .collect(Collectors.toList()));

        if(highestType.getHierarchy() >= user.getUserType().getHierarchy()) {
            return new ResponseEntity("cant delete a more privileged user", HttpStatus.FORBIDDEN);
        }

        userRepo.delete(user);
        return ResponseEntity.noContent().build();
    }
}
