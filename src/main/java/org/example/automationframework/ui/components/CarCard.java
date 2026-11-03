package org.example.automationframework.ui.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CarCard {

    private final SelenideElement rootElement;

    public CarCard(SelenideElement rootElement) {
        this.rootElement = rootElement;
    }

    @Step("Карточка авто: проверить наличие текста '{value}'")
    public void verifyText(String value) {
        rootElement.shouldHave(Condition.text(value).because("Проверка текста: " + value));
    }

    @Step("Карточка авто: нажать кнопку 'Записаться'")
    public void clickBookButton() {
        rootElement.$(By.xpath(".//button")).click();
    }
}