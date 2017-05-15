package com.bdev.smart.config.data;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SmartConfigValue<T> {
    private final String name;
    private final AtomicReference<T> value;

    private final CopyOnWriteArrayList<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

    public SmartConfigValue(String name, T value) {
        this.name = name;
        this.value = new AtomicReference<>(value);
    }

    public T getValue() {
        return value.get();
    }

    public String getName() {
        return name;
    }

    public void override(T newValue) {
        value.set(newValue);

        for (Consumer<T> listener : listeners) {
            try {
                listener.accept(newValue);
            } catch (Exception e) {
                /* Do nothing */
            }
        }
    }

    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
    }

    public boolean isNameSuitable(String name) {
        return this.name.equals(name);
    }
}
