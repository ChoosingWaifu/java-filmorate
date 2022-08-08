package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class MpaDao {

    private final Logger log = LoggerFactory.getLogger(MpaDao.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getById(int id) {
        String query = "SELECT MPA FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeMpa, id);
    }

    public Collection<Mpa> getAll() {
        String query = "SELECT * FROM MPA";
        return jdbcTemplate.query(query, this::makeMpa);
    }

    public Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
            return new Mpa(rs.getInt("MPA_ID"), (rs.getString("MPA")) );
        }

}
