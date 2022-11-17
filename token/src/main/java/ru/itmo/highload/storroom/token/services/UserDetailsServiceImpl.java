package ru.itmo.highload.storroom.token.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.token.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password;
        List<GrantedAuthority> authorities;

        if (username.equals(adminUsername)) {
            password = encoder.encode(adminPassword);
            authorities = getAuthorities(UserDTO.UserType.admin);
        } else {
            UserDTO userDTO = userService.getUser(username);
            if (userDTO == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
            password = userDTO.getPassword();
            authorities = getAuthorities(userDTO.getUserType());
        }

        return User
                .withUsername(username)
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public static List<GrantedAuthority> getAuthorities (UserDTO.UserType userType) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(UserDTO.UserType type : UserDTO.UserType.values()) {
            if (userType.getHierarchy() <= type.getHierarchy()) {
                authorities.add(new SimpleGrantedAuthority(type.name()));
            }
        }
        return authorities;
    }
}