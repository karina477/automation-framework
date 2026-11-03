package org.example.automationframework.tests.api;

import com.codeborne.selenide.junit5.SoftAssertsExtension;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.example.automationframework.base.BaseApiTest;
import org.example.automationframework.db.model.Car;
import org.example.automationframework.dto.*;
import org.example.automationframework.generator.DbGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SoftAssertsExtension.class)
public class CarBackendTest extends BaseApiTest {

    private String baseUrl = "http://localhost:8080";
    private String cars = "/api/v1/cars";
    private String bookings = "/api/v1/cars/%s/test-drive";

    @Test
    void testVerificationVehicleCardCreation() {
        CarRequestDto carRequestDto = new CarRequestDto(
                "1HGCR2F83HA000133",
                "Honda",
                "Accord",
                2025,
                "32500.00",
                0
        );

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)

                .when()
                .post(cars)
                .then()
                .statusCode(201)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("SUCCESS", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertNotNull(apiResponse.getResponseBody(), "Тело ответа responseBody не должно быть null!");

        dbStep.verifyCarsCount(1);
        assertEquals("AVAILABLE", dbStep.getCarStatus(apiResponse.getResponseBody().getId()));
    }

    @ParameterizedTest
    @CsvSource({
            "ghbf45632svbnh45j",
            "1HGCR2F83HA0001237",
            "1HGCR2F83HA00012",
            "1ПРНЕКАМИ4579ВЫУБ",
            "1HGCR2F83 A000123",
            "!@#$%^&*()_..№;:!",
            "1HOKJ2F83HA000123",
            "1HMIV2F83HA000123",
            "1HASQ2F83HA000123",
            "1HoHG2F83HA000123",
            "1HSiK2F83HA000123",
            "1HXCq2F83HA000123",
    })
    void testUnsuccessfulCreationOfVehicleRecordDueInvalidFieldVin(String vin) {
        CarRequestDto carRequestDto =
                new CarRequestDto(vin, "Honda", "Accord", 2025, "32500.00", 0);

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars)
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertEquals("Ошибка валидации входящих параметров.", apiResponse.getResponseMessage());
        assertEquals("VALIDATION_FAILED", apiResponse.getResponseError().getErrorCode());
        dbStep.verifyCarsCount(0);
    }

    @Test
    void testUnsuccessfulCreationOfVehicleRecordDueInvalidFieldMinYear() {
        CarRequestDto carRequestDto = new CarRequestDto(
                "1HGCR2F83HA000133",
                "Honda",
                "Accord",
                1899,
                "32500.00",
                0
        );

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars)
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertEquals("Ошибка валидации входящих параметров.", apiResponse.getResponseMessage());
        assertEquals("VALIDATION_FAILED", apiResponse.getResponseError().getErrorCode());
        dbStep.verifyCarsCount(0);
    }

    @Test
    void testUnsuccessfulCreationOfVehicleRecordDueInvalidFieldMaxYear() {
        CarRequestDto carRequestDto = new CarRequestDto(
                "1HGCR2F83HA000133",
                "Honda",
                "Accord",
                Year.now().plusYears(1).getValue(),
                "32500.00",
                0
        );

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars)
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertEquals("Ошибка валидации входящих параметров.", apiResponse.getResponseMessage());
        assertEquals("VALIDATION_FAILED", apiResponse.getResponseError().getErrorCode());
        dbStep.verifyCarsCount(0);
    }

    @ParameterizedTest
    @CsvSource({
            "-1",
            "0",
            "енгок",
            "№;%:?*"
    })
    void testUnsuccessfulCreationOfVehicleRecordDueInvalidFieldPrice(String price) {
        CarRequestDto carRequestDto =
                new CarRequestDto("1HGCR2F83HA000133", "Honda", "Accord", 2025, price, 0);

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars)
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertEquals("Ошибка валидации входящих параметров.", apiResponse.getResponseMessage());
        assertEquals("VALIDATION_FAILED", apiResponse.getResponseError().getErrorCode());
        dbStep.verifyCarsCount(0);
    }

    @Test
    void testReCreatingVehicleRecordWithIdenticalFieldVin() {
        CarRequestDto carRequestDto = new CarRequestDto(
                "1HGCR2F83HA000133",
                "Honda",
                "Accord",
                2025,
                "32500.00",
                0
        );

        given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars);

        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carRequestDto)
                .when()
                .post(cars)
                .then()
                .statusCode(409)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus(), "Некорректный статус ответа в конверте!");
        assertEquals("Регистрация автомобиля отклонена.", apiResponse.getResponseMessage());
        assertEquals("DUPLICATE_VIN", apiResponse.getResponseError().getErrorCode());
        dbStep.verifyCarsCount(1);
    }

    @Test
    void testCheckForDefaultValue() {
        dbStep.insertCars(DbGenerator.generateCars(11));
        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .get(cars)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals(10, apiResponse.getResponseBody().getCars().size());
    }

    @Test
    void testCheckForCorrectPageTurning() {
        List<Car> cars1 = DbGenerator.generateCars(2);
        dbStep.insertCars(cars1);
        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .get(cars + "?page=1&size=1")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals(cars1.get(1).getVin(), apiResponse.getResponseBody().getCars().get(0).getVin());
        assertEquals(1, apiResponse.getResponseBody().getPagination().getCurrentPage());
        assertEquals(1, apiResponse.getResponseBody().getPagination().getPageSize());
    }

    @Test
    void testCheckForRestriction() {
        List<Car> cars1 = DbGenerator.generateCars(101);
        dbStep.insertCars(cars1);
        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .get(cars + "?size=100")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals(100, apiResponse.getResponseBody().getPagination().getPageSize());
    }

    @ParameterizedTest
    @CsvSource({
            "300, 350, 1",
            "250, 300, 1",
            "301, 400, 0",
            "200, 299, 0"
    })
    void testFilteringTestWithValidData(String minPrice, String maxPrice, Integer countCars) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cars);
        stringBuilder.append("?minPrice=");
        stringBuilder.append(minPrice);
        stringBuilder.append("&maxPrice=");
        stringBuilder.append(maxPrice);
        Car car = new Car(
                "Honda",
                0,
                "Accord",
                300.00,
                "AVAILABLE",
                "1HGCR2F83HA000133",
                2025
        );

        dbStep.insertCar(car);
        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .get(stringBuilder.toString())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals(countCars, apiResponse.getResponseBody().getCars().size());
    }

    @Test
    void testFilteringErrorCheckMinPriceMoreMaxPrice(){
        ApiResponse<CarResponse> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .get(cars + "?minPrice=400&maxPrice=200")
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<ApiResponse<CarResponse>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Некорректные параметры фильтрации.", apiResponse.getResponseMessage());
        assertEquals("INVALID_PRICE_RANGE", apiResponse.getResponseError().getErrorCode());
        assertEquals("Минимальная цена не может быть выше максимальной.", apiResponse.getResponseError().getDetails());
    }

    @Test
    void testCheckTestDriveAppointment(){
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
        CarBookingRequestDto carBookingRequestDto = new CarBookingRequestDto(
                "Петр Petrov",
                "+79991112233",
                "petr@example.com",
                data,time
        );

        ApiResponse<CarBookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carBookingRequestDto)
                .log().all()
                .when()
                .post(String.format(bookings,id))
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ApiResponse<CarBookingResponseBody>>() {
                });

                assertEquals("SUCCESS", apiResponse.getResponseStatus());
                assertEquals(String.format("%sT%s:00Z",data,time), apiResponse.getResponseBody().getDateTime() );
    }

    @Test
    void testVerificationSelectingNonExistentIdCar(){
        CarBookingRequestDto carBookingRequestDto = new CarBookingRequestDto(
                "Петр Petrov",
                "+79991112233",
                "petr@example.com",
                "2026-09-02",
                "12:00"
        );

        ApiResponse<CarBookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carBookingRequestDto)
                .when()
                .post(String.format(bookings,"888"))
                .then()
                .log().all()
                .statusCode(404)
                .extract()
                .as(new TypeRef<ApiResponse<CarBookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Ошибка оформления записи.", apiResponse.getResponseMessage());
        assertEquals("CAR_NOT_FOUND", apiResponse.getResponseError().getErrorCode());
        assertEquals("Автомобиль с ID 888 не найден в системе.", apiResponse.getResponseError().getDetails());
    }

    @Test
    void testErrorCheckWhenReRegisteringForTestDriveWithSameFieldValueDateAndTime(){
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
        CarBookingRequestDto carBookingRequestDto = new CarBookingRequestDto(
                "Петр Petrov",
                "+79991112233",
                "petr@example.com",
                "2026-09-10",
                "12:00"
        );

        given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carBookingRequestDto)
                .when()
                .post(String.format(bookings,id));

        ApiResponse<CarBookingResponseBody> apiResponse = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(carBookingRequestDto)
                .when()
                .post(String.format(bookings,id))
                .then()
                .log().all()
                .statusCode(503)
                .extract()
                .as(new TypeRef<ApiResponse<CarBookingResponseBody>>() {
                });

        assertEquals("ERROR", apiResponse.getResponseStatus());
        assertEquals("Выбранное время недоступно.", apiResponse.getResponseMessage());
        assertEquals("TIME_SLOT_ALREADY_BOOKED",  apiResponse.getResponseError().getErrorCode());
        assertEquals("Вспомогательный сервис бронирования отклонил запрос. Данный час уже занят другим клиентом.", apiResponse.getResponseError().getDetails());
    }
}