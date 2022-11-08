package ru.itmo.highload.storroom.orders.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name="orders")
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @NonNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "finished_time")
    private LocalDateTime finishedTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    @NonNull
    private UnitEntity unit;

    @Column(name = "user_id", nullable = false)
    @NonNull
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id) && startTime.equals(that.startTime) && endTime.equals(that.endTime) && Objects.equals(finishedTime, that.finishedTime) && status == that.status && unit.equals(that.unit) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, finishedTime, status, unit, userId);
    }
}