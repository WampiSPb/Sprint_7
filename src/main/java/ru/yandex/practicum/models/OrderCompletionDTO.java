package ru.yandex.practicum.models;

public class OrderCompletionDTO {
    private Integer id;

    public OrderCompletionDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
