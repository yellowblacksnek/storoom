package ru.itmo.highload.storoom.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name="owners")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OwnerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    @Column(name = "owner_name")
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    @NonNull
    @Column(name = "company_id")
    private CompanyEntity company;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    @Column(name = "location_id")
    private List<LocationEntity> locations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnerEntity that = (OwnerEntity) o;
        return id.equals(that.id) && name.equals(that.name) && company.equals(that.company) && Objects.equals(locations, that.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, company, locations);
    }
}