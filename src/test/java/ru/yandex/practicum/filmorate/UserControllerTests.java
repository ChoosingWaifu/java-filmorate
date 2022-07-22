package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


@SpringBootTest
class UserControllerTests {

	UserController userController;
	User user;

	@BeforeEach
	void beforeEach() {
		userController = new UserController();
		user = new User();
		user.setEmail("w@ya.ru");
		user.setLogin("qwe");
		user.setName(" ");
		user.setBirthday(LocalDate.of(2000, 1, 1));
	}
	@Test
	void BirthdayValidationTest() {
		user.setBirthday(LocalDate.MAX);
		assertThrows(ValidationException.class,() -> userController.validate(user));
	}

	@Test
	void LoginValidationTest() {
		user.setLogin("we qwe");
		assertThrows(ValidationException.class,() -> userController.validate(user));
	}

	@Test
	void EmailCharValidationTest() {
		user.setEmail("test");
		assertThrows(ValidationException.class,() -> userController.validate(user));
	}

	@Test
	void EmptyEmailValidationTest() {
		user.setEmail(" ");
		assertThrows(ValidationException.class,() -> userController.validate(user));
	}

}
