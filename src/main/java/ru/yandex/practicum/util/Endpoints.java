package ru.yandex.practicum.util;

public class Endpoints {
    public static final String CREATING_A_COURIER = "/api/v1/courier";
    public static final String AUTHORIZATION_COURIER = "/api/v1/courier/login";
    public static final String COURIER_REMOVAL = "/api/v1/courier/{id}";
    public static final String CREATING_AN_ORDER = "/api/v1/orders";
    public static final String ORDER_CANCELLATION = "/api/v1/orders/cancel";
    public static final String GETTING_A_LIST_OF_ORDERS = "/api/v1/orders";
    public static final String RECEIVING_AN_ORDER_BY_NUMBER = "/api/v1/orders/track";
    public static final String ORDER_ACCEPTANCE = "/api/v1/orders/accept/{id}";
    public static final String ORDER_COMPLETION = "/api/v1/orders/finish/{id}";
}
