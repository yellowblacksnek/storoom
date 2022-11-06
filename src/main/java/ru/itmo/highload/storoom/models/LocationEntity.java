package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.LocationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name="locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LocationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @ManyToMany
    @JoinTable(
            name = "owners_locations",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id"))
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
