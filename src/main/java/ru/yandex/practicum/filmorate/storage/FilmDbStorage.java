package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
               return new Film(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("MPA.MPA_ID"), rs.getString("MPA.NAME")));
    }

    public Film getById(long id) {
            String query = "SELECT * FROM FILMS LEFT JOIN MPA M on FILMS.MPA_ID = M.MPA_ID WHERE FILMS.ID = ?";
            Film film = jdbcTemplate.queryForObject(query, this::makeFilm, id);
            film.getGenres().addAll(getFilmGenresById(film.getId()));
        log.info("get film by id {} with genres {}", film.getId(), film.getGenres());
            return film;
    }

    public Collection<Film> getAll() {
        String query = "SELECT * FROM FILMS LEFT JOIN MPA M on FILMS.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(query, this::makeFilm);
    }

    public void addFilm(Film film) {
        String sqlQuery = "insert into FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        addFilmGenres(film);
        log.info("add film {} with genres {}", film.getId(), film.getGenres());
    }
    public void updateFilm(Film film) {
        Set<Genre> uniqueGenre = new HashSet<>(film.getGenres());
        film.getGenres().clear();
        film.getGenres().addAll(uniqueGenre);
        deleteFilmGenres(film.getId());
        addFilmGenres(film);
        String sqlQuery = "update FILMS set " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId()
        );
       log.info("update film {} with genres {}", film.getId(), getFilmGenresById(film.getId()));
    }

    private Collection<Genre> getFilmGenresById(long id) {
        String query = "SELECT DISTINCT fg.GENRE_ID,g.NAME from FILMS_GENRES fg Join Genres g on fg.GENRE_ID = g.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(query, this::makeGenre, id);
    }

    private void addFilmGenres(Film film) {
        String query = "insert into FILMS_GENRES (FILM_ID, GENRE_ID) values (?, ?)";
        Set<Integer> genreIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        genreIds.forEach(o-> jdbcTemplate.update(query, film.getId(), o));
        log.info("addFilmGenres method {}", genreIds);
    }
    private void deleteFilmGenres(long id) {
        String query = "delete from FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(query, id);
    }

    public Mpa getMpaById(int id) {
        String query = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeMpa, id);
    }

    public Collection<Mpa> getAllMpa() {
        String query = "SELECT * FROM MPA";
        return jdbcTemplate.query(query, this::makeMpa);
    }

    public Genre getGenreById(int id) {
        String query = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeGenre, id);
    }

    public Collection<Genre> getAllGenre() {
        String query = "SELECT * FROM GENRES";
        return jdbcTemplate.query(query, this::makeGenre);
    }

    public Map<Long, Film> getFilms() {
        Collection<Film> getAll = getAll();
        Map<Long,Film> result = new HashMap<>();
        getAll.forEach(o -> result.put(o.getId(),o));
        return result;
    }

    public void addLike(long userId, long filmId) {
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) " +
                "VALUES (? ,?) ";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    public void deleteLike(long userId, long filmId) {
        String sqlQuery = "delete from LIKES" +
                " WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public Collection<Like> getAllLikes() {
        String sqlQuery = "SELECT * from LIKES";
        return jdbcTemplate.query(sqlQuery, this::makeLikes);
    }

    public Collection<Film> topFilms(int count) {
        String sqlQuery = "SELECT f.ID, COUNT(L.USER_ID)" +
                " FROM FILMS f left join LIKES L on f.ID = L.FILM_ID GROUP BY f.ID ORDER BY COUNT(L.USER_ID) DESC LIMIT ?";
        List<Long> result = jdbcTemplate.query(sqlQuery, this::makeRate, count);
        Collection<Film> returnResult = result.stream().map(this::getById).collect(Collectors.toList());
        log.info("result = {}, top {} films {}",result, count, returnResult);
        return returnResult;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), (rs.getString("NAME")) );
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), (rs.getString("NAME")) );
    }

    private Long makeRate(ResultSet rs, int rowNum) throws SQLException {
       return rs.getLong("ID");
    }

    private Like makeLikes(ResultSet rs, int rowNum) throws SQLException {
        return new Like(rs.getLong("FILM_ID"),rs.getLong("USER_ID"));
    }
}
