package ru.itmo.highload.storoom.models;

import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.LocationType;
import ru.itmo.highload.storoom.consts.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(name="owners")
public class OwnerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id", nullable = false)
    @NonNull
    private CompanyEntity company;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id", nullable = false)
    private List<LocationEntity> locations;

    public OwnerEntity(String name, CompanyEntity company, List<LocationEntity> locations) {
        this.name = name;
        this.company = company;
        this.locations = locations;
    }

    public OwnerEntity() {

    }
}