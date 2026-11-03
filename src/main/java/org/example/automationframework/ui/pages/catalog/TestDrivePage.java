package org.example.automationframework.ui.pages.catalog;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;

@Component
public class TestDrivePage {

    private final SelenideElement nameInput = $(By.xpath("//input[@id='book-name']"));
    private final SelenideElement phoneInput = $(By.xpath("//input[@id='book-phone']"));
    private final SelenideElement emailInput = $(By.xpath("//input[@id='book-email']"));
    private final SelenideElement dataInput = $(By.xpath("//input[@id='book-date']"));
    private final SelenideElement timeDropdown = $(By.xpath("//select[@id='book-time']"));
    private final SelenideElement submitBookingButton = $(By.xpath("//button[@id='btn-submit-booking']"));
    private final SelenideElement textTitle = $(By.xpath("//section[@id='page-confirmation']/h2"));
    private final SelenideElement errorEmailText = $(By.xpath("//div[@id='booking-error']"));

    @Step("Тест драйв: в поле имя ввели значение {value}")
    public void setName(String value) {
        nameInput.setValue(value);
    }

    @Step("Тест драйв: в поле номер телефона ввели значение {value}")
    public void setPhone(String value) {
        phoneInput.setValue(value);
    }

    @Step("Тест драйв: в поле email ввели значение {value}")
    public void setEmail(String value) {
        emailInput.setValue(value);
    }

    @Step("Тест драйв: в поле дата ввели значение {value}")
    public void setData(String value) {
        dataInput.setValue(value);
    }

    @Step("Тест драйв: в поле время ввели значение {value}")
    public void setTime(String value) {
        timeDropdown.selectOptionByValue(value);
    }

    @Step("Тест драйв: нажимаем на кнопку 'записаться'")
    public void clickSubmitBooking() {
        submitBookingButton.click();
    }

    @Step("Тест драйв: проверить наличие текста '{value}'")
    public void verifyText(String value) {
        textTitle.shouldHave(Condition.text(value).because("Проверка текста: " + value));
    }

    @Step("Тест драйв: проверить наличие текста '{value}' в поле email")
    public void verifyEmailText(String value) {
        errorEmailText.shouldHave(Condition.text(value).because("Проверка текста: " + value));
    }
}