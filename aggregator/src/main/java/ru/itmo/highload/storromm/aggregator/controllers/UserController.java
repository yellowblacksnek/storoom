package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
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
@Tag(name = "Users")
public class UserController {
    private final UserClient userClient;

    @GetMapping(params = "userType")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Get users", pageable = true, responseCodes = {400, 401, 403})
    @Parameter(name = "userType", description = "(optional) userType to search for")
    public ResponseEntity<Page<UserReadDTO>> getUsers(@RequestParam(required = false) String userType,
                                                    Pageable pageable) {
        if(userType == null) return userClient.getAll(pageable);
        else return userClient.getAllByType(userType, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Create user.", responseCodes = {400, 401, 403, 409})
    public ResponseEntity<UserReadDTO> createUser(@RequestBody UserFullDTO req) {
        return userClient.create(req);
    }

    @PutMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    @CustomizedOperation(description = "Update password by user's username.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UserReadDTO> updatePassword(@PathVariable String username,
                                                      @RequestBody UserPasswordDTO req) {
        return userClient.updatePassword(username, req);
    }

    @PutMapping("/{username}/type")
    @PreAuthorize("hasAuthority('admin') and #username != authentication.name")
    @CustomizedOperation(description = "Update userType by user's username.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UserReadDTO> updateUserType(@PathVariable String username,
                                                      @RequestBody UserUserTypeDTO req) {
        return userClient.updateUserType(username, req);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Delete user by username.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UserReadDTO> deleteByUsername(Authentication auth,
                                                        @PathVariable String username) {
        return userClient.deleteByUsername(username, getHighestAuthority(auth));
    }
}
