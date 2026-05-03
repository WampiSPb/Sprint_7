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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

public class CourierCreatingTests extends BaseTest {
    CourierSteps courierSteps = new CourierSteps();
    private Courier courier;
    private CourierCreatingDTO courierCreatingDTO;
    private CourierLoginDTO courierLoginDTO;
    private boolean isCourierCreated = false;

    @Before
    public void setUp() {
        courier = new Courier();
        courier.withLogin(RandomStringUtils.randomAlphabetic(12))
                .withPassword(RandomStringUtils.randomAlphabetic(10))
                .withFirstName(RandomStringUtils.randomAlphabetic(9));
        courierCreatingDTO = new CourierCreatingDTO(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        courierLoginDTO = new CourierLoginDTO(courier.getLogin(), courier.getPassword());
    }

    @Test
    @DisplayName("Тест: курьера можно создать, запрос возвращает правильный код ответа, возвращает ok: true")
    @Description("Позитивный тест для проверки ручки /api/v1/courier на создание курьера")
    public void canCreateCourierTest() {
        ValidatableResponse response = courierSteps.createCourier(courierCreatingDTO);
        response.statusCode(201).body("ok", is(true));
        isCourierCreated = true;
    }

    @Test
    @DisplayName("Тест: нельзя создать двух одинаковых курьеров")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание двух одинаковых курьеров")
    public void creatingTwoIdenticalCourierLoginTests() {
        ValidatableResponse response = courierSteps.createCourier(courierCreatingDTO);
        response.statusCode(201);
        isCourierCreated = true;

        Courier courier1 = new Courier();
        courier1.withLogin(courier.getLogin())
                .withPassword(RandomStringUtils.randomAlphabetic(10))
                .withFirstName(RandomStringUtils.randomAlphabetic(9));
        CourierCreatingDTO courierCreatingDTO1 = new CourierCreatingDTO(courier1.getLogin(), courier1.getPassword(), courier1.getFirstName());
        courierSteps.createCourier(courierCreatingDTO1)
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Тест: курьера нельзя создать, если не передать логин")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание курьера не передав логин")
    public void canNotCreateCourierWithoutLoginTest() {
        CourierCreatingDTO courierDTO = new CourierCreatingDTO("", courier.getPassword(), courier.getFirstName());
        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(400).body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест: курьера нельзя создать, если не передать пароль")
    @Description("Негативный тест для проверки ручки /api/v1/courier на создание курьера не передав пароль")
    public void canNotCreateCourierWithoutPasswordTest() {
        CourierCreatingDTO courierDTO = new CourierCreatingDTO(courier.getLogin(), "", courier.getFirstName());
        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(400).body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест: курьера можно создать даже без имени")
    @Description("Позитивный тест для проверки ручки /api/v1/courier на создание курьера не передав имя")
    public void canCreateCourierWithoutFirstNameTest() {
        CourierCreatingDTO courierDTO = new CourierCreatingDTO(courier.getLogin(), courier.getPassword(), "");
        ValidatableResponse response = courierSteps.createCourier(courierDTO);
        response.statusCode(201).body("ok", is(true));
        isCourierCreated = true;
    }

    @After
    public void tearDown() {
        if (isCourierCreated) {
            ValidatableResponse loginResponse = courierSteps.loginCourier(courierLoginDTO);
            if (loginResponse.extract().statusCode() == 200) {
                Integer id = loginResponse.extract().body().path("id");
                if (id != null) {
                    courierSteps.deleteCourier(id).statusCode(200);
                }
            }
        }
    }
}