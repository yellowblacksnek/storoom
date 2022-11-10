package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.highload.storoom.consts.*;

import java.time.LocalDateTime;
import java.util.List;
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
    public static class OwnerReadDTO {
        public UUID id;
        public String name;
        public UUID companyId;
        public List<UUID> locationIds;
    }
    @Data
    public static class OwnerDTO {
        public String name;
        public UUID companyId;
        public List<UUID> locationIds;
    }
    @Data
    public static class LocationReadDTO {
        public UUID id;
        public String address;
        public LocationType locationType;
        public List<UUID> ownerIds;
    }
    @Data
    public static class LocationDTO {
        public String address;
        public LocationType locationType;
        public List<UUID> ownerIds;
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
    public static class UnitFullDTO {
        public UUID id;
        public Integer sizeX, sizeY, sizeZ;
        public UnitType unitType;
        public UnitStatus status;
        public LocationReadDTO location;
        public LockFullDTO lock;
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

    @Data
    public static class OrderFullDTO {
        public UUID id;
        public LocalDateTime startTime;
        public LocalDateTime endTime;
        public LocalDateTime finishedTime;
        public OrderStatus status;
        public UnitFullDTO unit;
        public UserReadDTO user;
    }

    @Data
    public static class LockDTO {
        public UUID id;
        public String name;
        public UUID manufacturer;
    }

    @Data
    public static class LockFullDTO {
        public UUID id;
        public String name;
        public ManufacturerDTO manufacturer;
    }

    @Data
    public static class ManufacturerDTO {
        public UUID id;
        public String name;
    }
}
