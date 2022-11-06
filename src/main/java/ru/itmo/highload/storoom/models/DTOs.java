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
        public String id;
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

    @Data
    public static class UnitDTO {
        public String sizeX, sizeY, sizeZ;
        public String unitType;
        public String isAvailable;
        public String locationId;
        public String lockId;
    }

    @Data
    public static class OrderFullDTO {
        public UUID id;
        public Integer number;
        public Integer days;
        public UnitEntity unit;
        public UserEntity user;

    }

    @Data
    public static class OrderReadDTO {
        public Integer number;
        public Integer days;
        public UnitEntity unit;
        public UserEntity user;
    }
}
