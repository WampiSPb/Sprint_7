package ru.yandex.practicum.models;

public class OrderCancellationDTO {
    private Integer track;

    public OrderCancellationDTO(Integer track) {
        this.track = track;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }
}
