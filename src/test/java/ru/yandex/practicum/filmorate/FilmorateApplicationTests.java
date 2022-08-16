package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationTests {

    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;

    private User createUser1() {
        return new User(1,
                "1@email.ru",
                "1Login",
                "1Name",
                LocalDate.of(2000, 4, 21));
    }

    private User createSecondUser() {
        return new User(2,
                "2@email.ru",
                "2Login",
                "2Name",
                LocalDate.of(2000, 4, 21));
    }

    private User createThirdUser() {
        return new User(3,
                "3@email.ru",
                "3Login",
                "3Name",
                LocalDate.of(2000, 4, 21));
    }

    private Film createFilm1() {
        return new Film(1,
                "Film1",
                "Description1",
                LocalDate.of(2000, 4, 21),
                100,
                filmStorage.getMpaById(1));
    }

    private Film createFilm2() {
        return new Film(2,
                "Film2",
                "Description2",
                LocalDate.of(2000, 4, 21),
                100,
                filmStorage.getMpaById(2));
    }

    @Test
    public void addUserGetUserTest() {
        User user = createUser1();
        userStorage.addUser(user);
        assertEquals(1,userStorage.getAll().size(),"юзер не добавлен");
        assertEquals(user,userStorage.getById(user.getId()));
    }

    @Test
    public void addFilmGetTest() {
        Film film = createFilm1();
        filmStorage.addFilm(film);
        assertEquals(1,filmStorage.getAll().size(),"фильм не добавлен");
        assertEquals(film,filmStorage.getById(film.getId()));
    }

    @Test
    public void updateUserTest() {
        assertEquals("1Login",userStorage.getById(1).getLogin());
        User user = createUser1();
        user.setLogin("updateLogin1");
        userStorage.updateUser(user);
        assertEquals("updateLogin1",userStorage.getById(1).getLogin());
    }

    @Test
    public void updateFilmTest() {
        assertEquals("Film1",filmStorage.getById(1).getName());
        Film film = createFilm1();
        film.setName("updateFilm1");
        film.getGenres().add(filmStorage.getGenreById(1));
        filmStorage.updateFilm(film);
        assertEquals("updateFilm1",filmStorage.getById(1).getName(),"обновлено описание");
        assertEquals(1,filmStorage.getById(1).getGenres().size(),"размер нового списка жанров");
        assertEquals("Комедия",filmStorage.getById(1).getGenres().get(0).getName(),"название жанра");
    }

    @Test
    public void friendsAddDeleteCommonTest() {
        User user2 = createSecondUser();
        User user3 = createThirdUser();
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        userStorage.addFriend(1,2);
        assertEquals(1,userStorage.friendsById(1).size(),"размер списка друзей = 1");
        assertTrue(userStorage.friendsById(1).contains(user2),"друг с id = 2");
        userStorage.addFriend(1,3);
        assertEquals(2,userStorage.friendsById(1).size(),"размер списка друзей = 2");
        assertTrue(userStorage.friendsById(1).contains(user3),"друг с id = 3");
        userStorage.deleteFriend(1,2);
        assertEquals(1,userStorage.friendsById(1).size(),"размер списка друзей = 1");

        userStorage.addFriend(2, 3);
        assertEquals(1, userStorage.commonFriends(1, 2).size(),"размер списка общих друзей");
        assertTrue(userStorage.commonFriends(1, 2).contains(user3),"общий друг юзеров 1 и 2 = юзер 3");
    }

    @Test
    public void likesTest() {
        Film film2 = createFilm2();
        Like like1 = new Like(1,1);
        filmStorage.addFilm(film2);
        assertTrue(filmStorage.getAllLikes().isEmpty());
        filmStorage.addLike(1,1);
        assertTrue(filmStorage.getAllLikes().contains(like1), "добавление лайка");

        filmStorage.deleteLike(1,1);
        assertTrue(filmStorage.getAllLikes().isEmpty(),"список лайков пуст");

        filmStorage.addLike(1,2);
        assertTrue(filmStorage.topFilms(1).contains(filmStorage.getById(2)),"лучший фильм id=2");
    }

    @Test
    public void mpaTest() {
        assertEquals("G",filmStorage.getMpaById(1).getName(),"значение mpa по id");
        assertEquals(5,filmStorage.getAllMpa().size(),"число записей mpa");
    }

    @Test
    public void genreTest() {
        assertEquals("Комедия",filmStorage.getGenreById(1).getName(),"значение genre по id");
        assertEquals(6,filmStorage.getAllGenre().size(),"число записей genre");
    }
}
