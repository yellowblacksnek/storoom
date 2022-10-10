package ru.itmo.highload.storoom.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;
import ru.itmo.highload.storoom.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(getAuthorities(userEntity.getUserType()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private static List<GrantedAuthority> getAuthorities (UserType userType) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(UserType type : UserType.values()) {
            if (userType.getHierarchy() <= type.getHierarchy()) {
                authorities.add(new SimpleGrantedAuthority(type.name()));
            }
        }
        return authorities;
    }
}