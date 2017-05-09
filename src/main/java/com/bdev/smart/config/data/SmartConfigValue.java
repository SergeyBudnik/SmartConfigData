package com.bdev.smart.config.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SmartConfigValue<T> {
    @Getter private T value;

    private List<Consumer<T>> listeners = new ArrayList<>();

    public SmartConfigValue(T value) {
        this.value = value;
    }

    public void override(T newValue) {
        value = newValue;

        for (Consumer<T> listener : listeners) {
            try {
                listener.accept(value);
            } catch (Exception e) {
                /* Do nothing */
            }
        }
    }

    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
    }
}
