package ru.itmo.highload.storoom.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(name="units")
@NoArgsConstructor
@Getter @Setter
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
    @Column(name = "is_available")
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

    public UnitEntity(Integer sizeX, Integer sizeY, Integer sizeZ, LocationEntity location, Boolean isAvailable, LockEntity lock) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.location = location;
        this.isAvailable = isAvailable;
        this.unitType = calculateUnitType(sizeX, sizeY, sizeZ);
        this.lock = lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitEntity that = (UnitEntity) o;
        return id.equals(that.id) && sizeX.equals(that.sizeX) && sizeY.equals(that.sizeY) && sizeZ.equals(that.sizeZ) && unitType == that.unitType && Objects.equals(isAvailable, that.isAvailable) && location.equals(that.location) && lock.equals(that.lock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sizeX, sizeY, sizeZ, unitType, isAvailable, location, lock);
    }

    private UnitType calculateUnitType(Integer sizeX, Integer sizeY, Integer sizeZ){
        double volume = sizeX * sizeY * sizeZ / 10E6;
        if (volume < 0.05) return UnitType.S;
        else if (volume < 0.2) return UnitType.M;
        else if (volume < 0.8) return UnitType.L;
        return UnitType.XL;
    }

}