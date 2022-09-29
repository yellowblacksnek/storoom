package ru.itmo.highload.storoom.models;

import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="locks")
public class LockEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    @NonNull
    private ManufacturerEntity manufacturer;

    public LockEntity() {
    }
    public LockEntity(ManufacturerEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

}