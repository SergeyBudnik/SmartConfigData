package com.bdev.smart.config.data;

import java.util.Optional;

public interface CommonSmartConfig {
    <T> Optional<SmartConfigValue<T>> findPropertyByName(String propertyName);
}
