package ru.itmo.highload.storoom.models;

import lombok.*;
import org.hibernate.Hibernate;
import ru.itmo.highload.storoom.consts.UserType;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity userEntity = (UserEntity) o;
        return id != null && Objects.equals(id, userEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}