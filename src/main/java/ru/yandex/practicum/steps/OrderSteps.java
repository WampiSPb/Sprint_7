package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.*;

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

    @Step("Получение списка заказов")
    public ValidatableResponse gettingListOfOrders() {
        return given()
                .when()
                .get(GETTING_A_LIST_OF_ORDERS)
                .then();
    }

    @Step("Получение заказа по номеру")
    public ValidatableResponse gettingOrdersByItsNumber(Integer orderTrack) {
        return given()
                .when()
                .queryParam("t", orderTrack)
                .get(RECEIVING_AN_ORDER_BY_NUMBER)
                .then();
    }

    @Step("Принятие заказа")
    public ValidatableResponse acceptOrder(Integer courierId, Order order) {
        return given()
                .when()
                .pathParams("id", order.getId())
                .queryParam("courierId", courierId)
                .put(ORDER_ACCEPTANCE)
                .then();
    }

    @Step("Принятие заказа без Id курьера")
    public ValidatableResponse acceptWithoutCourierIdOrder(Order order) {
        return given()
                .when()
                .pathParams("id", order.getId())
                .put(ORDER_ACCEPTANCE)
                .then();
    }

    @Step("Принятие заказа без Id заказа")
    public ValidatableResponse acceptWithoutIdOrder(Courier courier) {
        return given()
                .when()
                .queryParam("courierId", courier.getCourierId())
                .put("/api/v1/orders/accept/")
                .then();
    }

    @Step("Завершение заказа")
    public ValidatableResponse completeOrder(OrderCompletionDTO orderCompletionDTO) {
        return given()
                .when()
                .pathParams("id", orderCompletionDTO.getId())
                .put(ORDER_COMPLETION)
                .then();
    }
}
