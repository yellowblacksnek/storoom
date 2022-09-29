package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="orders")
@AllArgsConstructor
@Data
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    @Column(name = "order_number")
    private Integer number;
    @NonNull
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    @NonNull
    @Column(name = "days")
    private Integer days;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    @NonNull
    @Column(name = "unit_id")
    private UnitEntity unit;

    public OrderEntity(Integer number, Integer days, UnitEntity unit) {
        this();
        this.number = number;
        this.days = days;
        this.unit = unit;
    }

    public OrderEntity() {
        this.createdTime = LocalDateTime.now();
    }
}