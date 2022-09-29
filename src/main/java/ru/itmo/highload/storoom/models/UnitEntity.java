package ru.itmo.highload.storoom.models;

import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="units")
public class UnitEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private Integer sizeX, sizeY, sizeZ;
    @NonNull
    @Enumerated(EnumType.STRING)
    private UnitType unitType;
    private Boolean isAvailable;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id", nullable = false)
    @NonNull
    private LocationEntity location;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lock_id", nullable = false)
    @NonNull
    private LockEntity lock;

    public UnitEntity(String name, Integer sizeX, Integer sizeY, Integer sizeZ, LocationEntity location, Boolean isAvailable, LockEntity lock) {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.location = location;
        this.isAvailable = isAvailable;
        this.unitType = calculateUnitType(sizeX, sizeY, sizeZ);
        this.lock = lock;
    }

    private UnitType calculateUnitType(Integer sizeX, Integer sizeY, Integer sizeZ){
        double volume = sizeX*sizeY*sizeZ/10E6;
        if (volume < 0.05) return UnitType.S;
        else if (volume < 0.2) return UnitType.M;
        else if (volume < 0.8) return UnitType.L;
        return UnitType.XL;
    }

    public UnitEntity() {
    }
}