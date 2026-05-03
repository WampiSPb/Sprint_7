package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
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
    OrderSteps orderSteps = new OrderSteps();
    private Order order;
    public OrderTestData data;
    private OrderCreatingDTO orderCreatingDTO;
    private boolean isOrderCreated = false;

    public OrderCreatingTest(OrderTestData data) {
        this.data = data;
    }

    @Parameterized.Parameters
    public static Collection<OrderTestData> provideData() {
        return OrderTestData.getTestData();
    }

    @Before
    public void setUp() {
        order = new Order();
        order.withFirstName(data.getFirstName()).withLastName(data.getLastName()).withAddress(data.getAddress())
                .withMetroStation(data.getMetroStation()).withPhone(data.getPhone()).withRentTime(data.getRentTime())
                .withDeliveryDate(data.getDeliveryDate()).withComment(data.getComment())
                .withColor(data.getColor());
        orderCreatingDTO = new OrderCreatingDTO(order.getFirstName(), order.getLastName(), order.getAddress(),
                order.getMetroStation(), order.getPhone(), order.getRentTime(), order.getDeliveryDate(),
                order.getComment(), order.getColor());
    }

    @Test
    @DisplayName("Тест: Проверь, что когда создаёшь заказ: 1. Можно указать один из цветов — BLACK или GREY; " +
            "2. Можно указать оба цвета; 3. Можно совсем не указывать цвет; 4. Тело ответа содержит track")
    @Description("Позитивный тест для проверки ручки /api/v1/orders на создание заказа с различными вариантами допустимых цветов скутера")
    public void creatingOrderWithDifferentScooterColorsTest() {
        ValidatableResponse response = orderSteps.createOrder(orderCreatingDTO);
        response.statusCode(201).body("track", notNullValue());
        order.withTrack(response.extract().body().path("track"));
        isOrderCreated = true;
    }

    @After
    public void tearDown() {
        // Отмена созданного заказа (если заказ был создан успешно)
        if (isOrderCreated && order.getTrack() != null) {
            try {
                OrderCancellationDTO orderCancellationDTO = new OrderCancellationDTO(order.getTrack());
                orderSteps.cancellationOrder(orderCancellationDTO).statusCode(200);
            } catch (AssertionError e) {
                // Игнорируем ошибку, если заказ не может быть отменен
                System.out.println("Could not cancel order with track: " + order.getTrack());
            }
        }
    }
}