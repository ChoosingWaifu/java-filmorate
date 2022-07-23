package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;


@SpringBootTest
class UserControllerTests {

	private UserController userController;
	private User user;

	@BeforeEach
	void beforeEach() {
		userController = new UserController(new UserService(new InMemoryUserStorage()));
		user = new User();
		user.setEmail("w@ya.ru");
		user.setLogin("qwe");
		user.setName(" ");
		user.setBirthday(LocalDate.of(2000, 1, 1));
	}
	@Test
	void BirthdayValidationTest() {
		user.setBirthday(LocalDate.MAX);
		Exception ex = assertThrows(ValidationException.class,() -> userController.validate(user));
		assertEquals("Birthday can't be in the future", ex.getMessage());
	}

	@Test
	void LoginValidationTest() {
		user.setLogin("we qwe");
		Exception ex = assertThrows(ValidationException.class,() -> userController.validate(user));
		assertEquals("Login can't contain spaces", ex.getMessage());
	}

	@Test
	void EmailCharValidationTest() {
		user.setEmail("test");
		Exception ex = assertThrows(ValidationException.class,() -> userController.validate(user));
		assertEquals("Email should contain @", ex.getMessage());
	}

	@Test
	void EmptyEmailValidationTest() {
		user.setEmail(" ");
		Exception ex = assertThrows(ValidationException.class,() -> userController.validate(user));
		assertEquals("Email can't be empty", ex.getMessage());
	}

}
