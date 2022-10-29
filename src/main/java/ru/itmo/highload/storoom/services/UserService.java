package ru.itmo.highload.storoom.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.exceptions.BadRequestException;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.models.DTOs.UserFullDTO;
import ru.itmo.highload.storoom.models.DTOs.UserReadDTO;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    public UserReadDTO createUser(UserFullDTO req) {
        if(userRepo.existsByUsername(req.getUsername()) || req.getUsername().equals(adminUsername)) {
            throw new ResourceAlreadyExistsException("username " + req.getUsername() + " already exists");
        }

        req.setPassword(encoder.encode(req.getPassword()));
        req.setUserType("client");

        UserEntity entity = userRepo.save(Mapper.toUserEntity(req));
        return Mapper.toUserReadDTO(entity);
    }

    public void changeUserPassword(String username, String password) {
        if (username.equals(adminUsername)) {
            throw new BadRequestException("cant mess with the admin");
        }

        if (password == null || password.isEmpty()) {
            throw new BadRequestException("no password provided");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadRequestException("username not found");
        }
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
    }

    public void changeUserType(String username, String userType) {
        if (username.equals(adminUsername)) {
            throw new BadRequestException("cant mess with the admin");
        }

        if (userType == null || userType.isEmpty()) {
            throw new BadRequestException("no type provided");
        }

        UserType type;
        try {
            type = UserType.valueOf(userType);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("incorrect type provided");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadRequestException("username not found");
        }
        user.setUserType(type);
        userRepo.save(user);
    }

    public void deleteUser(String username, List<UserType> callerAuthorities) {
        if (username.equals(adminUsername)) {
            throw new BadRequestException("cant mess with the admin");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadRequestException("username not found");
        }

        UserType highestType = UserType.getHighestOf(callerAuthorities);

        if(highestType.getHierarchy() >= user.getUserType().getHierarchy()) {
            throw new BadRequestException("cant delete a more privileged user");
        }

        userRepo.delete(user);
    }
}
