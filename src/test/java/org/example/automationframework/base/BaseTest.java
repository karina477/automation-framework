package org.example.automationframework.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.example.automationframework.config.AppConfig;
import org.example.automationframework.db.DbStep;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public abstract class BaseTest {

    @Autowired
    protected DbStep dbStep;

    @BeforeAll
    static void setupGlobalConfig() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true));

        Configuration.browserSize = "1920x1080";
        Configuration.headless = false;

        Configuration.assertionMode = com.codeborne.selenide.AssertionMode.SOFT;
    }

    @AfterEach
    void tearDown() {
        dbStep.deleteAllCars();
    }
}