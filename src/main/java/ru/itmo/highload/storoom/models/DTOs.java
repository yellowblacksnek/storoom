package ru.itmo.highload.storoom.models;

import lombok.Data;

import java.util.UUID;

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

    @Data
    public static class CompanyReadDTO {
        public UUID id;
        public String name;
    }

    @Data
    public static class CompanyDTO {
        public String name;
    }
}
