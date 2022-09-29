package ru.itmo.highload.storoom.models;

import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="companies")
public class CompanyEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;


    public CompanyEntity(String name) {
        this.name = name;
    }

    public CompanyEntity() {
    }
}