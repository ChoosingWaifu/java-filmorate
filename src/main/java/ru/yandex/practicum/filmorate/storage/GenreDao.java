package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenreDao {


    private final Logger log = LoggerFactory.getLogger(GenreDao.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getById(int id) {
        String query = "SELECT GENRE FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeGenre, id);
    }

    public List<Genre> getAll() {
        String query = "SELECT * FROM GENRES";
        return jdbcTemplate.query(query, this::makeGenre);
    }

    public Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), (rs.getString("GENRE")) );
    }


}
