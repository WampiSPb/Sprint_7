package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.CourierCreatingDTO;
import ru.yandex.practicum.models.CourierLoginDTO;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.util.Endpoints.*;

public class CourierSteps {

    @Step("Создание курьера в сервисе")
    public ValidatableResponse createCourier(CourierCreatingDTO courierCreatingDTO) {
        return given()
                .body(courierCreatingDTO)
                .when()
                .post(CREATING_A_COURIER)
                .then();
    }

    @Step("Авторизация курьера в сервисе")
    public ValidatableResponse loginCourier(CourierLoginDTO courierLoginDTO) {
        return given()
                .body(courierLoginDTO)
                .when()
                .post(AUTHORIZATION_COURIER)
                .then();
    }

    @Step("Удаление курьера из сервиса")
    public ValidatableResponse deleteCourier(Integer courierId) {
        return given()
                .when()
                .pathParams("id", courierId)
                .delete(COURIER_REMOVAL)
                .then();
    }
}