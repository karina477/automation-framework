package org.example.automationframework.ui.pages.catalog;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.automationframework.ui.components.CarCard;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Component
public class CatalogPage {

    private final SelenideElement brandInput = $(By.xpath("//input[@id='filter-brand']"));
    private final SelenideElement minPriceInput = $(By.xpath("//input[@id='filter-min-price']"));
    private final SelenideElement maxPriceInput = $(By.xpath("//input[@id='filter-max-price']"));
    private final SelenideElement applyButton = $(By.xpath("//button[@id='btn-apply-filters']"));
    private final ElementsCollection carCards = $$(By.xpath("//div[@class='car-card']"));

    public CarCard getCard(int index) {
        return new CarCard(carCards.get(index));
    }

    @Step("Каталог: в поле бренд ввели значение {value}")
    public void setBrand(String value) {
        brandInput.setValue(value);
    }

    @Step("Каталог: в поле цена от  ввели значение {value}")
    public void setMinPrice(String value) {
        minPriceInput.setValue(value);
    }

    @Step("Каталог: в поле цена до  ввели значение {value}")
    public void setMaxPrice(String value) {
        maxPriceInput.setValue(value);
    }

    @Step("Каталог: нажимаем на кнопку 'применить'")
    public void clickFilter() {
        applyButton.click();
    }
}