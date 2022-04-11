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

// спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
//        Configuration.browser = "chrome";
//        Configuration.holdBrowserOpen = true;
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        DataGenerator.RegistrationDto notRegisteredUser = getUser("active");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__title']").shouldHave(Condition.text("Ошибка"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
        DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");
        DataGenerator.sendRequest(blockedUser);
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__title']").shouldHave(Condition.text("Ошибка"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        String wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__title']").shouldHave(Condition.text("Ошибка"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        String wrongPassword = getRandomPassword();
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $(withText("Продолжить")).click();
        $("[class='notification__title']").shouldHave(Condition.text("Ошибка"));
    }
}