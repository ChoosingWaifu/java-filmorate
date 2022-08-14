package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

public class FilmorateApplicationTests {

    @SpringBootTest
    @AutoConfigureTestDatabase
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    class FilmoRateApplicationTests {
        private final UserDbStorage userStorage;

    }
}
