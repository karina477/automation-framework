package org.example.automationframework.dto;

public class BookingRequestDto {
    private Long carId;
    private String dateTime;

    public BookingRequestDto(Long carId, String dateTime) {
        this.carId = carId;
        this.dateTime = dateTime;
    }

    public BookingRequestDto() {
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
