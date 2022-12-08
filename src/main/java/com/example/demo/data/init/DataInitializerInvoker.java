package com.example.demo.data.init;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
class DataInitializerInvoker implements ApplicationRunner {

    private final @NonNull List<DataInitializer> initializers;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializers.forEach(DataInitializer::initialize);
    }

    /**
     * Fallback {@link DataInitializer} to make sure we always have a t least one {@link DataInitializer} in the
     * Application context so that the autowiring of {@link DataInitializer} into {@link DataInitializerInvoker} works.
     *
     * @author Firoz Fazil
     */
    @Component
    static class NoOpDataInitializer implements DataInitializer {


        @Override
        public void initialize() {
        }
    }
}
