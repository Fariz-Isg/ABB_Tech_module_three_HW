package org.abbtech.module3.repository;

import org.abbtech.module3.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .fullName(rs.getString("full_name"))
                    .balance(rs.getBigDecimal("balance"))
                    .build();
        }
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
            return Optional.ofNullable(user);
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public void updateBalance(Long userId, BigDecimal balance) {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
        jdbcTemplate.update(sql, balance, userId);
    }
}
