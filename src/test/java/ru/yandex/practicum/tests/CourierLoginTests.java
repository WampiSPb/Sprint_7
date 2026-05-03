package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.Courier;
import ru.yandex.practicum.models.CourierCreatingDTO;
import ru.yandex.practicum.models.CourierLoginDTO;
import ru.yandex.practicum.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;

public class CourierLoginTests extends BaseTest {
    CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private CourierCreatingDTO courierCreatingDTO;
    private CourierLoginDTO courierLoginDTO;

    @Before
    public void setUp() {
        courier = new Courier();
        courier.withLogin(RandomStringUtils.randomAlphabetic(12))
                .withPassword(RandomStringUtils.randomAlphabetic(10))
                .withFirstName(RandomStringUtils.randomAlphabetic(9));
        courierCreatingDTO = new CourierCreatingDTO(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        courierLoginDTO = new CourierLoginDTO(courier.getLogin(), courier.getPassword());
        courierSteps.createCourier(courierCreatingDTO).statusCode(201);
    }

    @Test
    @DisplayName("Тест: курьер может авторизоваться, успешный запрос возвращает id")
    @Description("Позитивный тест для проверки ручки /api/v1/courier/login на авторизацию курьера")
    public void canLoginCourierTest() {
        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если неправильно указать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера с неправильным логином")
    public void invalidLoginErrorTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO("Qwerty1234", courier.getPassword());
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если неправильно указать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера с неправильным паролем")
    public void invalidPasswordErrorTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO(courier.getLogin(), "Qwerty1234");
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без логина")
    public void notValueLoginTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO("", courier.getPassword());
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без пароля")
    public void notValuePasswordTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO(courier.getLogin(), "");
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать логин и пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без логина и пароля")
    public void notValueLoginAndPasswordTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO("", "");
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию несуществующего курьера")
    public void invalidLoginAndPasswordErrorTest() {
        CourierLoginDTO courierLoginDTO1 = new CourierLoginDTO("78DvrnytuRedceReg71", "78DvrnytuRedceReg71");
        courierSteps.loginCourier(courierLoginDTO1)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(200).body("id", notNullValue());
        courier.withCourierId(loginResponse.extract().body().path("id"));
    }

    @After
    public void tearDown() {
        if (courier.getCourierId() != null) {
            courierSteps.deleteCourier(courier.getCourierId()).statusCode(200);
        }
    }
}
