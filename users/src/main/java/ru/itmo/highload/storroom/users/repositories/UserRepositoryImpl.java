package ru.itmo.highload.storroom.users.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itmo.highload.storroom.users.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.users.models.UserEntity;
import ru.itmo.highload.storroom.users.models.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{
    private final JdbcTemplate jdbcTemplate;

    static class UserRowMapper implements RowMapper<UserEntity> {

        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserEntity entity = new UserEntity();
            entity.setId(UUID.fromString(rs.getString("id")));
            entity.setUsername(rs.getString("username"));
            entity.setPassword(rs.getString("password"));
            entity.setUserType(UserType.valueOf(rs.getString("user_type")));
            return entity;
        }
    }

    public UserEntity findById(UUID id) {
        String query = "SELECT * FROM USERS WHERE id = ?";
        List<UserEntity> res = jdbcTemplate.query(query, new UserRowMapper(), id);
        if (res.isEmpty()) throw new ResourceNotFoundException();
        return res.get(0);
    }

    @Override
    public UserEntity findByUsername(String username) {
        String query = "SELECT * FROM USERS WHERE username = ?";
        List<UserEntity> res = jdbcTemplate.query(query, new UserRowMapper(), username);
        if (res.isEmpty()) return null;
        return res.get(0);
    }

    @Override
    public List<UserEntity> findAll() {
        String query = "SELECT * FROM USERS";
        return jdbcTemplate.query(query, new UserRowMapper());
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        int total = count();
        String query = "SELECT * FROM USERS LIMIT ? OFFSET ?";
        List<UserEntity> res = jdbcTemplate.query(query, new UserRowMapper(), pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(res, pageable, total);
    }

    @Override
    public Page<UserEntity> findAllByUserType(Pageable pageable, UserType userType) {
        String rowCount = "SELECT COUNT(*) FROM USERS WHERE user_type = ?";
        Integer total = jdbcTemplate.queryForObject(rowCount, Integer.class, userType.toString());
        String query = "SELECT * FROM USERS WHERE user_type = ? LIMIT ? OFFSET ?";
        List<UserEntity> res = jdbcTemplate.query(query, new UserRowMapper(), userType.toString(), pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(res, pageable, total);
    }

    @Override
    public Boolean existsByUsername(String username) {
        String rowCount = "SELECT COUNT(*) FROM USERS WHERE username = ?";
        Integer total = jdbcTemplate.queryForObject(rowCount, Integer.class, username);
        return total > 0;
    }

    public Boolean existsById(UUID id) {
        String rowCount = "SELECT COUNT(*) FROM USERS WHERE id = ?";
        Integer total = jdbcTemplate.queryForObject(rowCount, Integer.class, id);
        return total > 0;
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (existsById(entity.getId())) {
            return update(entity);
        } else {
            return create(entity);
        }
    }

    private UserEntity create(UserEntity entity) {
        if(entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        jdbcTemplate.update("INSERT INTO USERS(id, username, password, user_type) VALUES (?, ?, ?, ?)",
                entity.getId(), entity.getUsername(), entity.getPassword(), entity.getUserType().toString());
        return findById(entity.getId());
    }

    private UserEntity update(UserEntity entity) {
        jdbcTemplate.update("UPDATE USERS SET username = ?, password = ?, user_type = ? where id = ?",
                entity.getUsername(), entity.getPassword(), entity.getUserType().toString(), entity.getId());
        return findById(entity.getId());
    }

    @Override
    public UserEntity delete(UserEntity entity) {
        UserEntity res = findById(entity.getId());
        String query = "DELETE FROM USERS WHERE id = ?";
        jdbcTemplate.update(query, entity.getId());
        return res;
    }

    @Override
    public int count() {
        String rowCount = "SELECT COUNT(*) FROM USERS";
        Integer total = jdbcTemplate.queryForObject(rowCount, Integer.class);
        return total;
    }
}
