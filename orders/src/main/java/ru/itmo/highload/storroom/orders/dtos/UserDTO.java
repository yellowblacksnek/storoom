package ru.itmo.highload.storroom.orders.dtos;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class UserDTO {
    public UUID id;
    public String username;
    public String password;
    public UserType userType;

    public enum UserType {
        admin(0), superuser(1), client(2);

        private final Integer hierarchy;

        UserType(final Integer hierarchy) {
            this.hierarchy = hierarchy;
        }

        public Integer getHierarchy() {
            return hierarchy;
        }

        public static UserType getHighestOf(Collection<UserType> types) {
            UserType highest = null;

            for(UserType cur : types) {
                if(highest == null) {
                    highest = cur;
                    continue;
                }
                if(cur.getHierarchy() < highest.getHierarchy()) {
                    highest = cur;
                }
            }
            return highest;
        }
    }
}
