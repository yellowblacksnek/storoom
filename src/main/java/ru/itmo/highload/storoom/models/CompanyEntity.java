package ru.itmo.highload.storoom.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="companies")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    @Column(name = "company_name")
    private String name;

}