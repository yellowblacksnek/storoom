package ru.itmo.highload.storoom.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "locks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LockEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    @NonNull
    private ManufacturerEntity manufacturer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockEntity that = (LockEntity) o;
        return id.equals(that.id) && manufacturer.equals(that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturer);
    }
}