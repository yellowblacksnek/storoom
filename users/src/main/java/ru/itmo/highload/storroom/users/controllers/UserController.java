package ru.itmo.highload.storroom.users.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storroom.users.dtos.UserPasswordDTO;
import ru.itmo.highload.storroom.users.dtos.UserUserTypeDTO;
import ru.itmo.highload.storroom.users.services.UserService;
import ru.itmo.highload.storroom.users.models.UserType;
import ru.itmo.highload.storroom.users.dtos.UserFullDTO;
import ru.itmo.highload.storroom.users.dtos.UserReadDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(params = "username")
//    @PreAuthorize("hasAuthority('service')")
    @ResponseStatus(HttpStatus.OK)
    public UserFullDTO getFullUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserReadDTO getReadUserById(@PathVariable UUID id){
        return userService.getReadUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserReadDTO> getAll(@ParameterObject Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping(params = "userType")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserReadDTO> getAllByType(@RequestParam String userType, @ParameterObject Pageable pageable) {
        return userService.getAllByType(userType, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserReadDTO> create(@RequestBody UserFullDTO req) {
        UserReadDTO res = userService.create(req);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{username}/password")
    @ResponseStatus(HttpStatus.OK)
    public UserReadDTO updatePassword(@PathVariable String username, @RequestBody UserPasswordDTO req) {
        return userService.updatePassword(username, req.getPassword());
    }

    @PutMapping("/{username}/type")
    @ResponseStatus(HttpStatus.OK)
    public UserReadDTO updateUserType(@PathVariable String username, @RequestBody UserUserTypeDTO req) {
        return userService.updateUserType(username, req.getUserType());
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserReadDTO deleteByUsername(@PathVariable String username, @RequestParam UserType callerAuthority) {
        return userService.deleteByUsername(username, callerAuthority);
    }
}
