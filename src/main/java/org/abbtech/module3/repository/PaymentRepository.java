package org.abbtech.module3.repository;

import org.abbtech.module3.model.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Payment.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("user_id"))
                    .amount(rs.getBigDecimal("amount"))
                    .status(rs.getString("status"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }

    public Long createPayment(Long userId, BigDecimal amount, String status) {
        String sql = "INSERT INTO payments (user_id, amount, status, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setBigDecimal(2, amount);
            ps.setString(3, status);
            return ps;
        }, keyHolder);

        Number key = (Number) keyHolder.getKeys().get("ID");
        return key.longValue();
    }

    public void updateStatus(Long paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, paymentId);
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new PaymentRowMapper());
    }

    public List<Payment> findByUserId(Long userId) {
        String sql = "SELECT * FROM payments WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new PaymentRowMapper(), userId);
    }

    public Optional<Payment> findById(Long paymentId) {
        String sql = "SELECT * FROM payments WHERE id = ?";

        try {
            Payment payment = jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), paymentId);
            return Optional.ofNullable(payment);
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
