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
public class CarPositiveTest extends BaseTest {

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

    @Test
    void testSwitchingBetweenCatalogAndAdmin() {
        Selenide.open(frontendUrl);
        String brand = "Honda";
        String model = "Accord";
        String price = "1000";
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand(brand);
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear("2025");
        adminDashboardPage.setPrice(price);
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.verifyText(brand);
        firstCard.verifyText(model);
        firstCard.verifyText("$1 000");
    }

    @ParameterizedTest
    @CsvSource({
            "300, 350, 350",
            "250, 300, 250",
            "251, 350, 350",
            "250, 349, 250"
    })
    void testCardFilter(String minPrice, String maxPrice, String expectedPrice) {
        String brand = "Honda";
        String model = "Accord";
        String year = "2025";
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand(brand);
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear(year);
        adminDashboardPage.setPrice("250");
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.setVin("1HGCR2F83HA000127");
        adminDashboardPage.setBrand(brand);
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear(year);
        adminDashboardPage.setPrice("350");
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        catalogPage.setBrand(brand);
        catalogPage.setMinPrice(minPrice);
        catalogPage.setMaxPrice(maxPrice);
        catalogPage.clickFilter();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.verifyText(expectedPrice);
    }

    @Test
    void testCardFilterBrand() {
        String brand = "Honda";
        String model = "Accord";
        String year = "2025";
        String price = "250";
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand(brand);
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear(year);
        adminDashboardPage.setPrice(price);
        adminDashboardPage.clickSaveCar();
        adminDashboardPage.setVin("1HGCR2F83HA000127");
        adminDashboardPage.setBrand("test");
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear(year);
        adminDashboardPage.setPrice(price);
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        catalogPage.setBrand(brand);
        catalogPage.clickFilter();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.verifyText(brand);
    }

    @Test
    void testSignUpForTestDrive() {
        String brand = "Honda";
        String model = "Accord";
        String year = "2025";
        String price = "250";
        Selenide.open(frontendUrl);
        navigation.switchToAdmin();
        adminDashboardPage.setVin("1HGCR2F83HA000126");
        adminDashboardPage.setBrand(brand);
        adminDashboardPage.setModel(model);
        adminDashboardPage.setYear(year);
        adminDashboardPage.setPrice(price);
        adminDashboardPage.clickSaveCar();
        navigation.switchToCatalog();
        CarCard firstCard = catalogPage.getCard(0);
        firstCard.clickBookButton();
        testDrivePage.setName("Карина");
        testDrivePage.setPhone("+79578456987");
        testDrivePage.setEmail("glyshitel.v@yandex.ru");
        testDrivePage.setData("10.10.2026");
        testDrivePage.setTime("12:00");
        testDrivePage.clickSubmitBooking();
        testDrivePage.verifyText("Подтверждение записи");
    }
}