package com.bdev.smart.config.data;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SmartConfigValue<T> {
    private final String name;
    private final AtomicReference<T> value;
    private final boolean readProtected;
    private final boolean overrideProtected;

    private final CopyOnWriteArrayList<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

    public SmartConfigValue(String name, T value, boolean readProtected, boolean overrideProtected) {
        this.name = name;
        this.value = new AtomicReference<>(value);

        this.readProtected = readProtected;
        this.overrideProtected = overrideProtected;
    }

    public T getValue() {
        return getValue(false);
    }

    public T forceGetValue() {
        return getValue(true);
    }

    private T getValue(boolean forceRead) {
        if (readProtected && !forceRead) {
            throw new RuntimeException("Unforced read is forbidden");
        }

        return value.get();
    }

    public String getName() {
        return name;
    }

    public void override(T newValue) {
        override(newValue, false);
    }

    public void forceOverride(T newValue) {
        override(newValue, true);
    }

    private void override(T newValue, boolean forceOverride) {
        if (overrideProtected && !forceOverride) {
            throw new RuntimeException("Unforced override is forbidden");
        }

        T processedNewValue = SmartConfigValueTypeChecker.process(value.get(), newValue);

        value.set(processedNewValue);

        for (Consumer<T> listener : listeners) {
            try {
                listener.accept(processedNewValue);
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
