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

import static org.hamcrest.Matchers.*;

public class CourierCreatingTests extends BaseTest {

    private static final int LOGIN_LENGTH = 12;
    private static final int PASSWORD_LENGTH = 10;
    private static final int FIRST_NAME_LENGTH = 9;

    private static final String ERROR_MESSAGE_INSUFFICIENT_DATA = "Недостаточно данных для создания учетной записи";
    private static final String ERROR_MESSAGE_LOGIN_ALREADY_USED = "Этот логин уже используется";

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private CourierLoginDTO courierLoginDTO;
    private boolean isCourierCreated = false;

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
    }

    @Test
    @DisplayName("Тест: курьера можно создать, запрос возвращает правильный код ответа, возвращает ok: true")
    @Description("Позитивный тест для проверки ручки /api/v1/courier на создание курьера")
    public void canCreateCourierTest() {
        CourierCreatingDTO courierCreatingDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        ValidatableResponse response = courierSteps.createCourier(courierCreatingDTO);
        response.statusCode(HttpStatus.SC_CREATED).body("ok", is(true));
        isCourierCreated = true;
    }

    @Test
    @DisplayName("Тест: нельзя создать двух одинаковых курьеров")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание двух одинаковых курьеров")
    public void creatingTwoIdenticalCourierLoginTests() {
        CourierCreatingDTO courierCreatingDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        ValidatableResponse response = courierSteps.createCourier(courierCreatingDTO);
        response.statusCode(HttpStatus.SC_CREATED);
        isCourierCreated = true;

        CourierCreatingDTO duplicateCreatingDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password(RandomStringUtils.randomAlphabetic(PASSWORD_LENGTH))
                .firstName(RandomStringUtils.randomAlphabetic(FIRST_NAME_LENGTH))
                .build();

        courierSteps.createCourier(duplicateCreatingDTO)
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", containsString(ERROR_MESSAGE_LOGIN_ALREADY_USED));
    }

    @Test
    @DisplayName("Тест: курьера нельзя создать, если не передать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание курьера не передав логин")
    public void canNotCreateCourierWithoutLoginTest() {
        CourierCreatingDTO courierDTO = CourierCreatingDTO.builder()
                .login("")
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(ERROR_MESSAGE_INSUFFICIENT_DATA));
    }

    @Test
    @DisplayName("Тест: курьера нельзя создать, если не передать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание курьера не передав пароль")
    public void canNotCreateCourierWithoutPasswordTest() {
        CourierCreatingDTO courierDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password("")
                .firstName(courier.getFirstName())
                .build();

        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(ERROR_MESSAGE_INSUFFICIENT_DATA));
    }

    @Test
    @DisplayName("Тест: курьера можно создать даже без имени")
    @Description("Позитивный тест для проверки ручки /api/v1/courier на создание курьера не передав имя")
    public void canCreateCourierWithoutFirstNameTest() {
        CourierCreatingDTO courierDTO = CourierCreatingDTO.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName("")
                .build();

        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(HttpStatus.SC_CREATED).body("ok", is(true));
        isCourierCreated = true;
    }

    @After
    public void tearDown() {
        if (isCourierCreated) {
            ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
            if (loginResponse.extract().statusCode() == HttpStatus.SC_OK) {
                Integer courierId = loginResponse.extract().body().path("id");
                if (courierId != null) {
                    courier.setCourierId(courierId);
                    courierSteps.deleteCourier(courier.getCourierId()).statusCode(HttpStatus.SC_OK);
                }
            }
        }
    }
}