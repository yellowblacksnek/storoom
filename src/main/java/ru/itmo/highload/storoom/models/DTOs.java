package ru.itmo.highload.storoom.models;

import lombok.Data;

public  class DTOs {
    @Data
    public static class UserFullDTO {
        public String username;
        public String password;
        public String userType;
    }

    @Data
    public static class UserReadDTO {
        public String username;
        public String userType;
    }
}
