package ru.naumen.javka.utils;

public abstract class TimeProvider {
    public abstract Long currentMillis();

    public Long currentSeconds() {
        return currentMillis() / 1000;
    }
}
