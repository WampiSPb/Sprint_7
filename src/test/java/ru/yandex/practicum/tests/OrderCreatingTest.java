package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.util.OrderTestData;
import ru.yandex.practicum.models.OrderCreatingDTO;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.OrderCancellationDTO;
import ru.yandex.practicum.steps.OrderSteps;

import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreatingTest extends BaseTest {

    private final OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private boolean isOrderCreated = false;

    private final OrderTestData testData;

    public OrderCreatingTest(OrderTestData testData) {
        this.testData = testData;
    }

    @Parameterized.Parameters(name = "Тест с данными: {0}")
    public static Collection<OrderTestData> provideData() {
        return OrderTestData.getTestData();
    }

    @Before
    public void setUp() {
        order = Order.builder()
                .firstName(testData.getFirstName())
                .lastName(testData.getLastName())
                .address(testData.getAddress())
                .metroStation(testData.getMetroStation())
                .phone(testData.getPhone())
                .rentTime(testData.getRentTime())
                .deliveryDate(testData.getDeliveryDate())
                .comment(testData.getComment())
                .color(testData.getColor())
                .build();
    }

    @Test
    @DisplayName("Тест создания заказа с различными вариантами цветов самоката")
    @Description("Проверка ручки /api/v1/orders на создание заказа с разными цветами: BLACK, GREY, оба цвета, без цвета")
    public void creatingOrderWithDifferentScooterColorsTest() {
        OrderCreatingDTO orderCreatingDTO = OrderCreatingDTO.builder()
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .address(order.getAddress())
                .metroStation(order.getMetroStation())
                .phone(order.getPhone())
                .rentTime(order.getRentTime())
                .deliveryDate(order.getDeliveryDate())
                .comment(order.getComment())
                .color(order.getColor())
                .build();

        ValidatableResponse response = orderSteps.createOrder(orderCreatingDTO);
        response.statusCode(HttpStatus.SC_CREATED).body("track", notNullValue());
        order.setTrack(response.extract().body().path("track"));
        isOrderCreated = true;
    }

    @After
    public void tearDown() {
        if (isOrderCreated && order.getTrack() != null) {
            try {
                OrderCancellationDTO orderCancellationDTO = OrderCancellationDTO.builder()
                        .track(order.getTrack())
                        .build();
                // Игнорируем возвращаемое значение, так как отмена может не поддерживаться
                orderSteps.cancellationOrder(orderCancellationDTO);
            } catch (Exception e) {
                System.out.println("Could not cancel order with track: " + order.getTrack());
            }
        }
    }
}