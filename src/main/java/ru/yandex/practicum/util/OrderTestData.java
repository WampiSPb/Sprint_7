package ru.yandex.practicum.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderTestData {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    private static final List<OrderTestData> TEST_DATA = Arrays.asList(
            new OrderTestData("Арина", "Иванова", "ул. Марата, д. 15, кв. 1",
                    "1", "+79991234567", 4, "2026-02-15",
                    "Оставить у двери", List.of("BLACK")),
            new OrderTestData("Марина", "Васильева", "ул Ленина, д 15, кв 3",
                    "2", "+79991234567", 3, "2026-02-15",
                    "", List.of("GREY")),
            new OrderTestData("Арина", "Иванова", "ул. Марата, д. 15, кв. 1",
                    "3", "+79991234567", 4, "2026-02-15",
                    "Оставить у двери", List.of("BLACK", "GREY")),
            new OrderTestData("Марина", "Васильева", "ул Ленина, д 15, кв 3",
                    "4", "+79991234567", 3, "2026-02-15",
                    "", List.of())
    );

    public static List<OrderTestData> getTestData() {
        return TEST_DATA;
    }
}