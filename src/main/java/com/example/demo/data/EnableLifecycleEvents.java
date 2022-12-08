package com.example.demo.data;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EnableLifecycleEvents.LifecycleEventConfiguration.class)
public @interface EnableLifecycleEvents {


    @Slf4j
    @ComponentScan
    @Configuration
    @EnableAutoConfiguration
    class LifecycleEventConfiguration {


        @PostConstruct
        public void init() {
            log.info("Enabling lifecycle events.");
        }
    }

}
