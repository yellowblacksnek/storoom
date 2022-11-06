package ru.itmo.highload.storoom.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.*;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UnitType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(name="units")
@NoArgsConstructor
@Getter @Setter
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.ALL)
public class UnitEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    @Column(name = "size_x")
    private Integer sizeX;
    @NonNull
    @Column(name = "size_y")
    private Integer sizeY;
    @NonNull
    @Column(name = "size_z")
    private Integer sizeZ;
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type")
    private UnitType unitType;

    @Column(name = "status")
    private UnitStatus status;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @NonNull
    private LocationEntity location;

    @OneToOne
    @JoinColumn(name = "lock_id", nullable = false)
    @NonNull
    private LockEntity lock;

    public UnitEntity(Integer sizeX, Integer sizeY, Integer sizeZ, LocationEntity location, UnitStatus status, LockEntity lock) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.location = location;
        this.status = status;
        this.unitType = calculateUnitType(sizeX, sizeY, sizeZ);
        this.lock = lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitEntity that = (UnitEntity) o;
        return id.equals(that.id) && sizeX.equals(that.sizeX) && sizeY.equals(that.sizeY) && sizeZ.equals(that.sizeZ) && unitType == that.unitType && Objects.equals(status, that.status) && location.equals(that.location) && lock.equals(that.lock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sizeX, sizeY, sizeZ, unitType, status, location, lock);
    }

    public void updateUnitType() {
        setUnitType(calculateUnitType(this.getSizeX(), this.getSizeY(), this.getSizeZ()));
    }

    private UnitType calculateUnitType(Integer sizeX, Integer sizeY, Integer sizeZ){
        double volume = sizeX * sizeY * sizeZ / 10E6;
        if (volume < 0.05) return UnitType.S;
        else if (volume < 0.2) return UnitType.M;
        else if (volume < 0.8) return UnitType.L;
        return UnitType.XL;
    }

}