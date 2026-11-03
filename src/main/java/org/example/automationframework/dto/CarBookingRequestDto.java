package org.example.automationframework.dto;

public class CarBookingRequestDto {

    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private String date;
    private String time;

    public CarBookingRequestDto() {
    }

    public CarBookingRequestDto(String clientName, String clientPhone, String clientEmail, String date, String time) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
        this.date = date;
        this.time = time;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
