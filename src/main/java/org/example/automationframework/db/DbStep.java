package org.example.automationframework.db;

import io.qameta.allure.Step;
import org.example.automationframework.db.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Component
public class DbStep {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbStep(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Step("БД: Проверить статус автомобиля с ID {carId}")
    public String getCarStatus(Long carId) {
        String sql = "SELECT status FROM main.cars WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, carId);
    }

    @Step("БД: удалить записи из бд")
    public void deleteAllCars() {
        String sql = "DELETE FROM main.cars";
        jdbcTemplate.update(sql);
    }

    @Step("БД: удалить бранирование из бд")
    public void deleteAllBookings() {
        String sql = "DELETE FROM booking.bookings";
        jdbcTemplate.update(sql);
    }

    @Step("Бд: получить количество записей в таблице cars")
    public int getCarsCount() {
        String sql = "SELECT COUNT(*) FROM main.cars";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Step("Проверить, что в таблице {expectedCount} записей")
    public void verifyCarsCount(int expectedCount) {
        int actualCount = getCarsCount();

        assertThat("Количество машин в БД не совпадает с ожидаемым!",
                actualCount, is(expectedCount));
    }

    @Step("БД: Добавить машину")
    public Long insertCar(Car car) {
        SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cars")
                .withSchemaName("main")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("brand", car.getBrand());
        parameters.put("mileage", car.getMileage());
        parameters.put("model", car.getModel());
        parameters.put("price", car.getPrice());
        parameters.put("status", car.getStatus());
        parameters.put("vin", car.getVin());
        parameters.put("year", car.getYear());
        parameters.put("created_at", LocalDateTime.now());

        Number deployedId = simpleInsert.executeAndReturnKey(parameters);

        return deployedId.longValue();
    }


    @Step("БД: Добавить список машин ({cars.size()} шт.)")
    public void insertCars(List<Car> cars) {
        String sql = "INSERT INTO main.cars (brand, mileage, model, price, status, vin, year, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, cars, cars.size(), (ps, car) -> {
            ps.setString(1, car.getBrand());
            ps.setInt(2, car.getMileage() != null ? car.getMileage() : 0);
            ps.setString(3, car.getModel());
            ps.setDouble(4, car.getPrice() != null ? car.getPrice() : 0.0);
            ps.setString(5, car.getStatus());
            ps.setString(6, car.getVin());
            ps.setInt(7, car.getYear() != null ? car.getYear() : 2026);

            java.time.LocalDateTime time =
                    car.getCreatedAt() != null ? car.getCreatedAt() : java.time.LocalDateTime.now();
            ps.setObject(8, time);
        });
    }
}