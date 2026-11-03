package org.example.automationframework.tests.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.junit5.SoftAssertsExtension;
import org.example.automationframework.base.BaseTest;
import org.example.automationframework.ui.components.CarCard;
import org.example.automationframework.ui.components.Navigation;
import org.example.automationframework.ui.pages.admin.AdminDashboardPage;
import org.example.automationframework.ui.pages.catalog.CatalogPage;
import org.example.automationframework.ui.pages.catalog.TestDrivePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(SoftAssertsExtension.class)
public class CarNegativeTest extends BaseTest {

    @Autowired
    private TestDrivePage testDrivePage;

    @Autowired
    private Navigation navigation;

    @Autowired
    private CatalogPage catalogPage;

    @Autowired
    private AdminDashboardPage adminDashboardPage;

    @Value("${url.frontend}")
    private String frontendUrl;

    @ParameterizedTest
    @CsvSource({
            "1HGCR2F83HA0001237, VIN-номер должен состоять строго из 17 символов",
            "1HGCR2F83HA00012, VIN-номер должен состоять строго из 17 символов",
            "1ПРНЕКАМИ4579ВЫУБ,VIN-номер должен состоять строго из 17 символов ",
            "1HGCR2F83 A000123, VIN-номер должен состоять строго из 17 символов",
            "!@#$%^&*()_./№;:!, VIN-номер должен состоять строго из 17 символов",
            "1HOKJ2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы",
            "1HMIV2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы",
            "1HASQ2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы",
            "1HoHG2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы",
            "1HSiK2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы",
            "1HXCq2F83HA000123, VIN должен состоять из 17 символов и не содержать латинские буквы"
    })
    void testIncorrectVin(String vin, String errorText) {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin(vin);
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("1000");
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.verifyVinText(errorText);
    }

    @Test
    void testIncorrectYearMin() {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("1899");
        adminDashboardPage.setPrice("1000");
        adminDashboardPage.clickSaveCar();
        dbStep.verifyCarsCount(0);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, Цена должна быть строго больше 0",
            "0, Цена должна быть строго больше 0"
    })
    void testErrorCheckingPriceGreaterThanZero(String price, String errorText) {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice(price);
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.verifyPriceText(errorText);
    }

    @Test
    void testErrorCheckPriceMustBeSpecified() {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("");
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.verifyPriceText("Цена должна быть указана");
    }

    @Test
    void testCheckForAbsenceOfDuplicateVehiclesWithSameVin() {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000124");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("1000");
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.setVin("1HGCR2F83HA000124");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("1000");
        adminDashboardPage.clickSaveCar();
        dbStep.verifyCarsCount(1);
    }

    @Test
    void testValidationCheckForIncomingParametersWithEmptyFieldEmail() {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("250");
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.clickBookButton();
        testDrivePage.setName("Карина");
        testDrivePage.setPhone("+79578456987");
        testDrivePage.setEmail("");
        testDrivePage.setData("10.10.2026");
        testDrivePage.setTime("12:00");
        testDrivePage.clickSubmitBooking();
        testDrivePage.verifyEmailText("Ошибка валидации входящих параметров.");
    }

    @ParameterizedTest
    @CsvSource({
            "glyshitel.vyandex.ru, Ошибка валидации входящих параметров.",
            "glyshitel.v@, Ошибка валидации входящих параметров.",
            "@yandex.ru, Ошибка валидации входящих параметров."
    })
    void testValidationCheckForIncomingParameterErrorsInFieldEmail(String email, String errorText) {
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand("Honda");
        adminDashboardPage.setModel("Accord");
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice("250");
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.clickBookButton();
        testDrivePage.setName("Карина");
        testDrivePage.setPhone("+79578456987");
        testDrivePage.setEmail(email);
        testDrivePage.setData("10.10.2026");
        testDrivePage.setTime("12:00");
        testDrivePage.clickSubmitBooking();
        testDrivePage.verifyEmailText(errorText);
    }
}