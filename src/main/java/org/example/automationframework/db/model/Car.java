package org.example.automationframework.db.model;

import java.time.LocalDateTime;

public class Car {
    private Long id;
    private String brand;
    private LocalDateTime createdAt;
    private Integer mileage;
    private String model;
    private Double price;
    private String status;
    private String vin;
    private Integer year;

    public Car() {
    }

    public Car(String brand, Integer mileage, String model, Double price, String status, String vin, Integer year) {
        this.brand = brand;
        this.mileage = mileage;
        this.model = model;
        this.price = price;
        this.status = status;
        this.vin = vin;
        this.year = year;
    }

    public Car(
            Long id,
            String brand,
            LocalDateTime createdAt,
            Integer mileage,
            String model,
            Double price,
            String status,
            String vin,
            Integer year
    ) {
        this.id = id;
        this.brand = brand;
        this.createdAt = createdAt;
        this.mileage = mileage;
        this.model = model;
        this.price = price;
        this.status = status;
        this.vin = vin;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}