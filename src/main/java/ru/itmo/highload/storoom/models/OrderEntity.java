package ru.itmo.highload.storoom.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name="orders")
@AllArgsConstructor
@Getter @Setter
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
    @JoinColumn(name = "unit_id", nullable = false)
    @NonNull
    private UnitEntity unit;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NonNull
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id) && number.equals(that.number) && createdTime.equals(that.createdTime) && days.equals(that.days) && unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, createdTime, days, unit);
    }

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