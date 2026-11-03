package org.example.automationframework.base;

import com.codeborne.selenide.Configuration;
import org.assertj.core.api.SoftAssertions;
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
public abstract class BaseApiTest {

    protected static SoftAssertions softly;

    @Autowired
    protected DbStep dbStep;

    @BeforeAll
    static void setupGlobalConfig() {
        softly = new SoftAssertions();
        Configuration.assertionMode = com.codeborne.selenide.AssertionMode.SOFT;
    }

    @AfterEach
    void tearDown() {
        dbStep.deleteAllCars();
        dbStep.deleteAllBookings();
        softly.assertAll();
    }
}