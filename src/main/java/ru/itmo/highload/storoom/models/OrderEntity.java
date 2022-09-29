package ru.itmo.highload.storoom.models;

import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.itmo.highload.storoom.consts.UnitType;
import ru.itmo.highload.storoom.models.LocationEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="orders")
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private Integer number;
    @NonNull
    private LocalDateTime createdTime;
    @NonNull
    private Integer days;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "unit_id", nullable = false)
    @NonNull
    private UnitEntity unit;

    public OrderEntity(Integer number, Integer days, UnitEntity unit) {
        this();
        this.number = number;
        this.days = days;
        this.unit = unit;
    }

    public OrderEntity(Integer number, Integer days, UnitEntity unit, LocalDateTime createdTime) {
        this.number = number;
        this.days = days;
        this.unit = unit;
        this.createdTime = createdTime;
    }

    public OrderEntity() {
        this.createdTime = LocalDateTime.now();
    }
}