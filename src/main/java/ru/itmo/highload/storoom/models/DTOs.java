package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.highload.storoom.consts.OrderStatus;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UnitType;
import ru.itmo.highload.storoom.consts.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

public  class DTOs {
    @Data
    public static class UserFullDTO {
        public String username;
        public String password;
        public UserType userType;
    }

    @Data
    public static class UserReadDTO {
        public UUID id;
        public String username;
        public UserType userType;
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
        public UUID id;
        public Integer sizeX, sizeY, sizeZ;
        public UnitType unitType;
        public UnitStatus status;
        public UUID locationId;
        public UUID lockId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDTO {
        public UUID id;
        public LocalDateTime startTime;
        public LocalDateTime endTime;
        public LocalDateTime finishedTime;
        public OrderStatus status;
        public UUID unitId;
        public UUID userId;

    }
}
