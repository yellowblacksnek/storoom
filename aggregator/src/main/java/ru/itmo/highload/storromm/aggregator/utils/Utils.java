package ru.itmo.highload.storromm.aggregator.utils;

import org.springframework.security.core.Authentication;
import ru.itmo.highload.storromm.aggregator.models.UserType;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static String getHighestAuthority(Authentication auth) {
        List<UserType> authorities = auth.getAuthorities().stream()
                .map(i -> UserType.valueOf(i.toString()))
                .collect(Collectors.toList());
        UserType highest = UserType.getHighestOf(authorities);
        return highest.toString();
    }
}
