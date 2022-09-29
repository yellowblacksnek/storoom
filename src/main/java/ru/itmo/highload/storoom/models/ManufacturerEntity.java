package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="manufacturers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManufacturerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;

}