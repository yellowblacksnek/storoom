package ru.itmo.highload.storoom.models;

import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="manufacturers")
public class ManufacturerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;


    public ManufacturerEntity() {
    }
    public ManufacturerEntity(String name) {
        this.name = name;
    }

}