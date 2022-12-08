package com.example.demo.data.event;

import java.time.LocalDateTime;

public interface Event {

    LocalDateTime getPublicationDate();

    Class<?> getAggregateType();
}
