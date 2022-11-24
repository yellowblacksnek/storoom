package ru.itmo.highload.storroom.locations.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Table(name = "owners")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnerEntity implements Serializable {
    @Id
    private UUID id;
    @NonNull
    @Column("owner_name")
    private String name;

    @Column("company_id")
    @NonNull
    private UUID companyId;

    @Transient
    private CompanyEntity company;

    @Transient
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