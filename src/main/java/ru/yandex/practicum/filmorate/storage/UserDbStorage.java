package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    public User getById(long id) {
        String query = "SELECT * FROM USERS WHERE USERS.ID = ?";
        return jdbcTemplate.queryForObject(query, this::makeUser, id);
    }

    public Collection<User> getAll() {
        String query = "SELECT * FROM USERS";
        return jdbcTemplate.query(query, this::makeUser);
    }

    public void addUser(User user) {
        String sqlQuery = "insert into USERS(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    public void updateUser(User user) {
        String sqlQuery = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId()
        );
    }

    public Map<Long, User> getUsers() {
        Collection<User> getAll = getAll();
        Map<Long,User> result = new HashMap<>();
        getAll.forEach(o -> result.put(o.getId(),o));
        return result;
    }

    public void addFriend(long userId, long friendId) {
        String sqlQuery = "insert into FRIENDSHIP (USER_ID, FRIEND_ID)" +
                "VALUES (? ,?) ";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("add friend {} -> {}", userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        String sqlQuery = "delete from FRIENDSHIP" +
                " WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("delete friend {} -> {}", userId, friendId);
    }

    public Collection<User> friendsById(long id) {
        String sqlQuery = "SELECT FRIEND_ID from FRIENDSHIP" +
                " WHERE USER_ID = ?";
        List<Long> result = jdbcTemplate.query(sqlQuery, this::makeFriends, id);
        log.info("friends by id {}, {} ",id, result.stream().map(this::getById).collect(Collectors.toList()));
        return result.stream().map(this::getById).collect(Collectors.toList());
    }

    public List<User> commonFriends(long userId, long friendId) {
        Collection<User> userFriends = friendsById(userId);
        Collection<User> friendFriends = friendsById(friendId);
        List<Long> uId = userFriends.stream().map(User::getId).collect(Collectors.toList());
        List<Long> fId = friendFriends.stream().map(User::getId).collect(Collectors.toList());
        List<Long> cId = uId.stream().filter(fId::contains).collect(Collectors.toList());
        List<User> result = new ArrayList<>();
        for(Long id: cId) {
            result.add(getById(id));
        }
        log.info("user friends - {}, friend friends - {}, common friends {}, {}",userFriends, friendFriends, userId, result);
        return result;
    }
    public Long makeFriends(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("FRIEND_ID");
    }
}
