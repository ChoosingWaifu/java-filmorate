package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StatusDbStorage {

    private final Logger log = LoggerFactory.getLogger(StatusDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public StatusDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Status getById(int id) {
        String query = "SELECT STATUS_ID, STATUS FROM STATUS WHERE STATUS_ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeStatus, id);
    }

    public Collection<Status> getAll() {
        String query = "SELECT * FROM STATUS";
        return jdbcTemplate.query(query, this::makeStatus);
    }
    public Status makeStatus(ResultSet rs, int rowNum) throws SQLException {
        return new Status(rs.getInt("STATUS_ID"), (rs.getString("STATUS")) );
    }

}
