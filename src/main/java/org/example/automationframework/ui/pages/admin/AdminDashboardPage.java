package org.example.automationframework.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;

@Component
public class AdminDashboardPage {

    private final SelenideElement vinInput = $(By.xpath("//input[@id='admin-vin']"));
    private final SelenideElement brandInput = $(By.xpath("//input[@id='admin-brand']"));
    private final SelenideElement modelInput = $(By.xpath("//input[@id='admin-model']"));
    private final SelenideElement yearInput = $(By.xpath("//input[@id='admin-year']"));
    private final SelenideElement priceInput = $(By.xpath("//input[@id='admin-price']"));
    private final SelenideElement saveCarButton = $(By.xpath("//button[@data-testid='admin-save-car']"));
    private final SelenideElement errorVinText = $(By.xpath("//div[@id='error-vin']"));
    private final SelenideElement errorPriceText = $(By.xpath("//div[@id='error-price']"));

    @Step("Админка: в поле vin ввели значение {value}")
    public void setVin(String value) {
        vinInput.setValue(value);
    }

    @Step("Админка: в поле бренд ввели значение {value}")
    public void setBrand(String value) {
        brandInput.setValue(value);
    }

    @Step("Админка: в поле модель ввели значение {value}")
    public void setModel(String value) {
        modelInput.setValue(value);
    }

    @Step("Админка: в поле год выпуска ввели значение {value}")
    public void setYear(String value) {
        yearInput.setValue(value);
    }

    @Step("Админка: в поле цена ввели значение {value}")
    public void setPrice(String value) {
        priceInput.setValue(value);
    }

    @Step("Админка: нажимаем на кнопку 'Сохранить автомобиль'")
    public void clickSaveCar() {
        saveCarButton.click();
    }

    @Step("Админка: проверить наличие текста '{value}' в ошибке поля vin")
    public void verifyVinText(String value) {
        errorVinText.shouldHave(Condition.partialText(value).because("Проверка текста: " + value));
    }

    @Step("Админка: проверить наличие текста '{value}' в ошибке поля price")
    public void verifyPriceText(String value) {
        errorPriceText.shouldHave(Condition.partialText(value).because("Проверка текста: " + value));
    }
}