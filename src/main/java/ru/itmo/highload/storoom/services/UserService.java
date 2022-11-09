package ru.itmo.highload.storoom.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.models.DTOs.UserFullDTO;
import ru.itmo.highload.storoom.models.DTOs.UserReadDTO;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    public UserEntity getRef(UUID id) {
        return userRepo.getOne(id);
    }

    public Page<UserReadDTO> getAll(Pageable pageable) {
        Page<UserEntity> res = userRepo.findAll(pageable);
        return res.map(Mapper::toUserReadDTO);
    }

    public Page<UserReadDTO> getAllByType(@RequestParam String userType, Pageable pageable) {
        Page<UserEntity> res = userRepo.findAllByUserType(pageable, UserType.valueOf(userType));
        return res.map(Mapper::toUserReadDTO);
    }

    public UserReadDTO create(UserFullDTO req) {
        if(userRepo.existsByUsername(req.getUsername()) || req.getUsername().equals(adminUsername)) {
            throw new ResourceAlreadyExistsException("username " + req.getUsername() + " already exists");
        }

        req.setPassword(encoder.encode(req.getPassword()));
        req.setUserType(UserType.client);

        UserEntity entity = userRepo.save(Mapper.toUserEntity(req));
        return Mapper.toUserReadDTO(entity);
    }

    public void updatePassword(String username, String password) {
        if (username.equals(adminUsername)) {
            throw new IllegalArgumentException("cant mess with the admin");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("no password provided");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("username not found");
        }
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
    }

    public void updateUserType(String username, UserType type) {
        if (username.equals(adminUsername)) {
            throw new IllegalArgumentException("cant mess with the admin");
        }

        if (type == null) {
            throw new IllegalArgumentException("no type provided");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("username not found");
        }
        user.setUserType(type);
        userRepo.save(user);
    }

    public void deleteByUsername(String username, List<UserType> callerAuthorities) {
        if (username.equals(adminUsername)) {
            throw new IllegalArgumentException("cant mess with the admin");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("username not found");
        }

        UserType highestType = UserType.getHighestOf(callerAuthorities);

        if(highestType.getHierarchy() >= user.getUserType().getHierarchy()) {
            throw new IllegalArgumentException("cant delete a more privileged user");
        }

        userRepo.delete(user);
    }
}
