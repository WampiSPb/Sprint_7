package ru.yandex.practicum.util;

import java.util.Arrays;
import java.util.List;

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

    public OrderTestData(String firstName, String lastName, String address, String metroStation, String phone,
                         int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    private static final List<OrderTestData> testData = Arrays.asList(
            new OrderTestData("Арина", "Иванова", "ул. Марата, д. 15, кв. 1",
                    "1", "+79991234567", 4, "2026-02-15",  // Исправлено: YYYY-MM-DD
                    "Оставить у двери", List.of("BLACK")),


            new OrderTestData("Марина", "Васильева", "ул Ленина, д 15, кв 3",
                    "2", "89991234567", 3, "2026-02-15",  // Исправлено: YYYY-MM-DD
                    "", List.of("GREY")),

            new OrderTestData("Арина", "Иванова", "ул. Марата, д. 15, кв. 1",
                    "3", "+79991234567", 4, "2026-02-15",  // Исправлено: YYYY-MM-DD
                    "Оставить у двери", List.of("BLACK", "GREY")),


            new OrderTestData("Марина", "Васильева", "ул Ленина, д 15, кв 3",
                    "4", "89991234567", 3, "2026-02-15",  // Исправлено: YYYY-MM-DD
                    "", List.of())
    );

    public static List<OrderTestData> getTestData() {
        return testData;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public List<String> getColor() {
        return color;
    }
}