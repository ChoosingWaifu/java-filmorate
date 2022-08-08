package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    private final MpaDao mpaDao;

    private final GenreDao genreDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        mpaDao = new MpaDao(jdbcTemplate);
        genreDao = new GenreDao(jdbcTemplate);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
               return new Film(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release").toLocalDate(),
                rs.getInt("duration"),
                genreDao.getById(rs.getInt("GENRE_ID")),
                mpaDao.getById(rs.getInt("MPA_ID")));
    }

    public Film getById(long id) {
            String query = "SELECT * FROM FILMS WHERE FILMS.ID = ?";
            return jdbcTemplate.queryForObject(query, this::makeFilm, id);
    }

    public Collection<Film> getAll() {
        String query = "SELECT * FROM FILMS";
        return jdbcTemplate.query(query, this::makeFilm);
    }

    public void addFilm(Film film) {
        String sqlQuery = "insert into FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, GENRE_ID, MPA_ID) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getMpa_id());
            stmt.setInt(6, film.getGenre().getGenre_id());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

    }
    public void updateFilm(Film film) {
        String sqlQuery = "update FILMS set " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, GENRE_ID = ?, MPA_ID = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getGenre().getGenre_id()
                , film.getMpa().getMpa_id()
                , film.getId()
        );

    }

    public Map<Long, Film> getFilms() {
        Collection<Film> getAll = getAll();
        Map<Long,Film> result = new HashMap<>();
        getAll.forEach(o -> result.put(o.getId(),o));
        return result;
    }
}
