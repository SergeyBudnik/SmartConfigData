package com.bdev.smart.config.data;

import com.bdev.smart.config.data.SmartConfigValueTypeChecker.Type;
import com.bdev.smart.config.data.wrappers.string.SmartConfigStringValueWrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SmartConfigValue<T> {
    private final CommonSmartConfig commonSmartConfig;
    private final String name;
    private final AtomicReference<T> value;

    private final CopyOnWriteArrayList<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

    public SmartConfigValue(CommonSmartConfig commonSmartConfig, String name, T value) {
        this.commonSmartConfig = commonSmartConfig;
        this.name = name;
        this.value = new AtomicReference<>(value);
    }

    public T getValue() {
        return resolveValue(value.get());
    }

    public String getName() {
        return name;
    }

    public void override(T newValue) {
        T processedNewValue = SmartConfigValueTypeChecker.process(name, value.get(), newValue);

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

    private T resolveValue(T value) {
        Type type = SmartConfigValueTypeChecker.getType(value);

        if (type == Type.STRING) {
            return (T) new SmartConfigStringValueWrapper(
                    (String) value,
                    env -> Optional.ofNullable(getResolvable(env))
            ).getValue();
        } else if (type == Type.LIST_OF_STRINGS) {
            return (T) ((List<String>) value)
                    .stream()
                    .map(it -> new SmartConfigStringValueWrapper(
                                    it,
                                    env -> Optional.ofNullable(getResolvable(env))
                            ).getValue()
                    )
                    .collect(Collectors.toList());
        } else {
            return value;
        }
    }

    private String getResolvable(String name) {
        String env = System.getenv(name);
        String vm = System.getProperty(name);

        if (env != null) {
            return env;
        } else if (vm != null) {
            return vm;
        } else {
            return commonSmartConfig
                    .findPropertyByName(name)
                    .map(it -> it.getValue().toString())
                    .orElse(null);
        }
    }
}
