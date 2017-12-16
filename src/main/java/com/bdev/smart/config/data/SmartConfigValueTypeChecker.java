package com.bdev.smart.config.data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

class SmartConfigValueTypeChecker {
    @SuppressWarnings("unchecked")
    static <T> T process(String name, T oldValue, T newValue) {
        Function<T, Boolean> isNumeric = it -> it instanceof Integer || it instanceof Long;

        boolean isSameClass = oldValue.getClass() == newValue.getClass();
        boolean isBothNumeric = isNumeric.apply(oldValue) && isNumeric.apply(newValue);
        boolean isList = newValue instanceof List;

        if (!isSameClass && !isBothNumeric && !isList) {
            throw new RuntimeException("Types mismatch for property: '" + name + "'");
        }

        if (isList) {
            Object oldValueElement = ((List) oldValue).get(0);

            for (Object newValueElement : (List) newValue) {
                if (oldValueElement.getClass() != newValueElement.getClass()) {
                    throw new RuntimeException();
                }
            }

            return (T) Collections.unmodifiableList((List) newValue);
        } else if (isBothNumeric){
            return (T) Long.valueOf(newValue.toString());
        } else {
            return newValue;
        }
    }
}
