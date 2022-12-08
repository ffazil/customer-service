package com.example.demo.data.init;

import org.springframework.transaction.annotation.Transactional;

public interface DataInitializer {

    /**
     * Called on application startup to trigger data initialization. Will run inside a transaction.
     */
    @Transactional
    void initialize();
}
