package ru.itmo.highload.storroom.orders.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
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

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_status")
    private UnitStatus status;

    @Column(name = "location_id", nullable = false)
    @NonNull
    private UUID locationId;

    @Column(name = "lock_id", nullable = false)
    @NonNull
    private UUID lockId;

    public UnitEntity(Integer sizeX, Integer sizeY, Integer sizeZ, UUID locationId, UnitStatus status, UUID lockId) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.locationId = locationId;
        this.status = status;
        this.unitType = calculateUnitType(sizeX, sizeY, sizeZ);
        this.lockId = lockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitEntity that = (UnitEntity) o;
        return id.equals(that.id) && sizeX.equals(that.sizeX) && sizeY.equals(that.sizeY) && sizeZ.equals(that.sizeZ) && unitType == that.unitType && Objects.equals(status, that.status) && locationId.equals(that.locationId) && lockId.equals(that.lockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sizeX, sizeY, sizeZ, unitType, status, locationId, lockId);
    }

    public void updateUnitType() {
        setUnitType(calculateUnitType(this.getSizeX(), this.getSizeY(), this.getSizeZ()));
    }

    private UnitType calculateUnitType(Integer sizeX, Integer sizeY, Integer sizeZ){
        if(sizeX == null | sizeY == null | sizeZ == null) return null;
        double volume = sizeX * sizeY * sizeZ / 10E6;
        if (volume < 0.05) return UnitType.S;
        else if (volume < 0.2) return UnitType.M;
        else if (volume < 0.8) return UnitType.L;
        return UnitType.XL;
    }

}