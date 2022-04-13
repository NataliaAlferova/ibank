package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.*;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;


public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        DataGenerator.RegistrationDto notRegisteredUser = getUser("active");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");
        DataGenerator.sendRequest(blockedUser);
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        String wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        String wrongPassword = getRandomPassword();
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $(withText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}