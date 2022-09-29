package ru.itmo.highload.storoom.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.LocationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(name="locations")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    @Column(name = "owner_id")
    private List<OwnerEntity> owners;
}
