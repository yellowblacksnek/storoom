package ru.itmo.highload.storroom.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.highload.storroom.users.utils.Mapper;
import ru.itmo.highload.storroom.users.models.UserType;
import ru.itmo.highload.storroom.users.dtos.UserFullDTO;
import ru.itmo.highload.storroom.users.dtos.UserReadDTO;
import ru.itmo.highload.storroom.users.exceptions.BadRequestException;
import ru.itmo.highload.storroom.users.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storroom.users.models.UserEntity;
import ru.itmo.highload.storroom.users.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    @Value("${admin.username}")
    private String adminUsername;

    public UserFullDTO getUserByUsername(String username) {
        UserEntity res = userRepo.findByUsername(username);
        return Mapper.toUserFullDTO(res);
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

    public void updateUserType(String username, UserType type) {
        if (username.equals(adminUsername)) {
            throw new BadRequestException("cant mess with the admin");
        }

        if (type == null) {
            throw new BadRequestException("no type provided");
        }

        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadRequestException("username not found");
        }
        user.setUserType(type);
        userRepo.save(user);
    }

    public void deleteByUsername(String username, List<UserType> callerAuthorities) {
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


