package ru.itmo.highload.storroom.locations.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "owners")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnerLocationEntity {
    @Column("owner_id")
    public UUID ownerId;
    @Column("location_if")
    public UUID locationId;
}
