package ru.naumen.javka.utils;

public class LiveTimeProvider extends TimeProvider {
    public LiveTimeProvider() {

    }

    @Override
    public Long currentMillis() {
        return System.currentTimeMillis();
    }
}
