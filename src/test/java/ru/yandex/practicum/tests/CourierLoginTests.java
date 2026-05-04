package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
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

    private static final int LOGIN_LENGTH = 12;
    private static final int PASSWORD_LENGTH = 10;
    private static final int FIRST_NAME_LENGTH = 9;

    private static final String ERROR_MESSAGE_ACCOUNT_NOT_FOUND = "Учетная запись не найдена";
    private static final String ERROR_MESSAGE_INSUFFICIENT_DATA = "Недостаточно данных для входа";

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private CourierLoginDTO courierLoginDTO;

    @Before
    public void setUp() {
        courier = Courier.builder()
                .login(RandomStringUtils.randomAlphabetic(LOGIN_LENGTH))
                .password(RandomStringUtils.randomAlphabetic(PASSWORD_LENGTH))
                .firstName(RandomStringUtils.randomAlphabetic(FIRST_NAME_LENGTH))
                .build();

        courierLoginDTO = CourierLoginDTO.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        CourierCreatingDTO courierCreatingDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        courierSteps.createCourier(courierCreatingDTO).statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("Тест: курьер может авторизоваться, успешный запрос возвращает id")
    @Description("Позитивный тест для проверки ручки /api/v1/courier/login на авторизацию курьера")
    public void canLoginCourierTest() {
        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если неправильно указать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера с неправильным логином")
    public void invalidLoginErrorTest() {
        CourierLoginDTO invalidLoginDTO = CourierLoginDTO.builder()
                .login("InvalidLogin123")
                .password(courier.getPassword())
                .build();

        courierSteps.loginCourier(invalidLoginDTO)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", containsString(ERROR_MESSAGE_ACCOUNT_NOT_FOUND));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если неправильно указать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера с неправильным паролем")
    public void invalidPasswordErrorTest() {
        CourierLoginDTO invalidPasswordDTO = CourierLoginDTO.builder()
                .login(courier.getLogin())
                .password("InvalidPassword123")
                .build();

        courierSteps.loginCourier(invalidPasswordDTO)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", containsString(ERROR_MESSAGE_ACCOUNT_NOT_FOUND));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без логина")
    public void notValueLoginTest() {
        CourierLoginDTO emptyLoginDTO = CourierLoginDTO.builder()
                .login("")
                .password(courier.getPassword())
                .build();

        courierSteps.loginCourier(emptyLoginDTO)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(ERROR_MESSAGE_INSUFFICIENT_DATA));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без пароля")
    public void notValuePasswordTest() {
        CourierLoginDTO emptyPasswordDTO = CourierLoginDTO.builder()
                .login(courier.getLogin())
                .password("")
                .build();

        courierSteps.loginCourier(emptyPasswordDTO)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(ERROR_MESSAGE_INSUFFICIENT_DATA));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: система вернёт ошибку, если не указать логин и пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию курьера без логина и пароля")
    public void notValueLoginAndPasswordTest() {
        CourierLoginDTO emptyDTO = CourierLoginDTO.builder()
                .login("")
                .password("")
                .build();

        courierSteps.loginCourier(emptyDTO)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(ERROR_MESSAGE_INSUFFICIENT_DATA));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Тест: авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Description("Негативный тест для проверки ручки /api/v1/courier/login на авторизацию несуществующего курьера")
    public void invalidLoginAndPasswordErrorTest() {
        CourierLoginDTO nonExistentDTO = CourierLoginDTO.builder()
                .login("NonExistentLogin123")
                .password("NonExistentPassword123")
                .build();

        courierSteps.loginCourier(nonExistentDTO)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", containsString(ERROR_MESSAGE_ACCOUNT_NOT_FOUND));

        ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
        loginResponse.statusCode(HttpStatus.SC_OK).body("id", notNullValue());
        courier.setCourierId(loginResponse.extract().body().path("id"));
    }

    @After
    public void tearDown() {
        if (courier.getCourierId() != null) {
            courierSteps.deleteCourier(courier.getCourierId()).statusCode(HttpStatus.SC_OK);
        }
    }
}