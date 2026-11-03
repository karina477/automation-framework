package org.example.automationframework.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.example.automationframework")
@PropertySource("classpath:application.properties")
public class AppConfig { }
