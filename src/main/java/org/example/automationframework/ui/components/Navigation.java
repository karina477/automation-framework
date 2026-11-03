package org.example.automationframework.ui.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;

@Component
public class Navigation {

    private final SelenideElement catalogTabButton = $(By.xpath("//button[@id='nav-catalog']"));
    private final SelenideElement adminTabButton = $(By.xpath("//button[@id='nav-admin']"));

    @Step("Навигация: переключиться на вкладку 'Каталог'")
    public void switchToCatalog() {
        catalogTabButton.shouldBe(Condition.visible).click();
    }

    @Step("Навигация: переключиться на вкладку 'Админка'")
    public void switchToAdmin() {
        adminTabButton.shouldBe(Condition.visible).click();
    }
}