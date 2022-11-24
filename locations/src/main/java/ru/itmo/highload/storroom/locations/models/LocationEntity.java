package ru.itmo.highload.storroom.locations.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationEntity implements Serializable {
    @Id
    private UUID id;

    @Column("address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column("location_type")
    private LocationType locationType;

    @Transient
    private List<OwnerEntity> owners;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity that = (LocationEntity) o;
        return id.equals(that.id) && Objects.equals(address, that.address) && locationType == that.locationType && Objects.equals(owners, that.owners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, locationType, owners);
    }
}
