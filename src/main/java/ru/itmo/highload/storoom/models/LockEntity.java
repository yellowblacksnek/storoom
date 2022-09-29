package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="locks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LockEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    @NonNull
    @Column(name = "manufacturer_id")
    private ManufacturerEntity manufacturer;

}