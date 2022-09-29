package ru.itmo.highload.storoom.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.LocationType;
import ru.itmo.highload.storoom.consts.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(name="locations")
public class LocationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String address;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", nullable = false)
    private List<OwnerEntity> owners;

    public LocationEntity(String address, UnitType unitType, List<OwnerEntity> owners) {
        this.address = address;
        this.owners = owners;
    }

    public LocationEntity() {
    }
}