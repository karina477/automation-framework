package org.example.automationframework.generator;

import org.example.automationframework.db.model.Car;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DbGenerator {

    public static List<Car> generateCars(int count) {

        List<Car> cars = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Car car = new Car();
            car.setBrand(TestDataGenerator.generateLetters(5));
            car.setModel(TestDataGenerator.generateLetters(5));
            car.setVin(TestDataGenerator.generateDigits(17));
            car.setYear(2025);
            car.setCreatedAt(LocalDateTime.now());
            car.setMileage(0);
            car.setPrice(Double.valueOf(TestDataGenerator.generateDigits(4)));
            car.setStatus("AVAILABLE");
            cars.add(car);
        }

        return cars;
    }
}