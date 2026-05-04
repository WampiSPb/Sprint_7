package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.OrderCancellationDTO;
import ru.yandex.practicum.models.OrderCreatingDTO;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.util.Endpoints.*;

public class OrderSteps {

    @Step("Создание заказа в сервисе")
    public ValidatableResponse createOrder(OrderCreatingDTO orderCreatingDTO) {
        return given()
                .body(orderCreatingDTO)
                .when()
                .post(CREATING_AN_ORDER)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancellationOrder(OrderCancellationDTO orderCancellationDTO) {
        return given()
                .body(orderCancellationDTO)
                .when()
                .put(ORDER_CANCELLATION)
                .then();
    }
}