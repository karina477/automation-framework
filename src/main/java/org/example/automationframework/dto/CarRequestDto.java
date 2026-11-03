package org.example.automationframework.dto;

public class CarRequestDto {

    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private String price;
    private Integer mileage;

    public CarRequestDto(String vin, String brand, String model, Integer year, String price, Integer mileage) {
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) { this.price = price; }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
}