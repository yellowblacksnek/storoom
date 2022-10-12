package ru.itmo.highload.storoom.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password;
        List<GrantedAuthority> authorities;

        if (username.equals(adminUsername)) {
            password = encoder.encode(adminPassword);
            authorities = getAuthorities(UserType.admin);
        } else {
            UserEntity userEntity = userRepository.findByUsername(username);
            if (userEntity == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
            password = userEntity.getPassword();
            authorities = getAuthorities(userEntity.getUserType());
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .authorities(authorities)
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