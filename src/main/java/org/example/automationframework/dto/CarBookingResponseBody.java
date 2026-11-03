package org.example.automationframework.dto;

public class CarBookingResponseBody {
    private String bookingReference;
    private Long carId;
    private String dateTime;
    private String clientName;
    private ExternalServices externalServices;

    public CarBookingResponseBody() {
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ExternalServices getExternalServices() {
        return externalServices;
    }

    public void setExternalServices(ExternalServices externalServices) {
        this.externalServices = externalServices;
    }
}