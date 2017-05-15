package com.bdev.smart.config.data;

import java.util.Collections;
import java.util.List;

class SmartConfigValueTypeChecker {
    @SuppressWarnings("unchecked")
    static <T> T process(T oldValue, T newValue) {
        boolean isSameClass = oldValue.getClass() == newValue.getClass();
        boolean isList = newValue instanceof List;

        if (!isSameClass && !isList) {
            throw new RuntimeException();
        }

        if (newValue instanceof List) {
            Object oldValueElement = ((List) oldValue).get(0);

            for (Object newValueElement : (List) newValue) {
                if (oldValueElement.getClass() != newValueElement.getClass()) {
                    throw new RuntimeException();
                }
            }

            return (T) Collections.unmodifiableList((List) newValue);
        } else {
            return newValue;
        }
    }
}
