package org.example.automationframework.tests.api;

import com.codeborne.selenide.junit5.SoftAssertsExtension;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.example.automationframework.base.BaseApiTest;
import org.example.automationframework.db.model.Car;
import org.example.automationframework.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SoftAssertsExtension.class)
public class CarBookingTest extends BaseApiTest {
    private String baseUrl = "http://localhost:8082";
    private String cars = "/api/v1/bookings";

    @Test
    void testInvalidFormatDateAndTime() {
        String time = "10:00";
        String data = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Ошибка валидации времени.", apiResponse.getResponseMessage());
        assertEquals("INVALID_DATE_FORMAT", apiResponse.getResponseError().getErrorCode());
        assertEquals("Некорректный формат времени.", apiResponse.getResponseError().getDetails());
    }

    @Test
    void testBookingForOddTime() {
        String time = "10:15";
        String data = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Некорректный интервал.", apiResponse.getResponseMessage());
        assertEquals("INVALID_TIME_SLOT", apiResponse.getResponseError().getErrorCode());
        assertEquals("Запись возможна только на ровные часы.", apiResponse.getResponseError().getDetails());
    }

    @Test
    void testSuccessfulTimeReservation() {
        String time = "10:00";
        String data = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .log().all()
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("SUCCESS", apiResponse.getResponseStatus());
        assertEquals("Слот времени успешно зарезервирован.", apiResponse.getResponseMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "08:00",
            "21:00"
    })
    void testCheckForInvalidWorkingHours(String time) {
        String data = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Время вне рабочих часов.", apiResponse.getResponseMessage());
        assertEquals("OUT_OF_WORKING_HOURS", apiResponse.getResponseError().getErrorCode());
        assertEquals("Автосалон закрыт в это время. Выберите часы с 09:00 до 20:00.", apiResponse.getResponseError().getDetails());
    }

    @Test
    void testPastTenseCheck() {
        String time = "10:00";
        String data = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .log().all()
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Отказ в резервировании.", apiResponse.getResponseMessage());
        assertEquals("PAST_DATE_FORBIDDEN", apiResponse.getResponseError().getErrorCode());
        assertTrue(apiResponse.getResponseError().getDetails().contains("Выбранное время уже прошло! Текущее время на сервере"));
    }

    @Test
    void testErrorCheckWhenReBookingTimeSlotWithSameFieldValueDateTime() {
        String time = "10:00";
        String data = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        Long id = dbStep.insertCar(car);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(id, String.format("%sT%s:00Z", data, time));
        given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .log().all()
                .when()
                .post(cars);

        ApiResponse<BookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(bookingRequestDto)
                .log().all()
                .when()
                .post(cars)
                .then()
                .log().all()
                .statusCode(409)
                .extract()
                .as(new TypeRef<ApiResponse<BookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Время уже занято.", apiResponse.getResponseMessage());
        assertEquals("SLOT_ALREADY_TAKEN", apiResponse.getResponseError().getErrorCode());
        assertEquals("Этот автомобиль уже забронирован на указанное время другим клиентом.", apiResponse.getResponseError().getDetails());
    }
}