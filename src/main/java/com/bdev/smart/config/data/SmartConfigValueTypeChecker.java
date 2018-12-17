package com.bdev.smart.config.data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

class SmartConfigValueTypeChecker {
    public enum Type {
        STRING, NUMBER, BOOLEAN, LIST_OF_STRINGS, LIST_OF_NUMBERS, LIST_OF_BOOLEANS
    }

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

    static <T> Type getType(T value) {
        boolean isString = value instanceof String;
        boolean isNumeric = value instanceof Integer || value instanceof Long;
        boolean isBoolean = value instanceof Boolean;

        boolean isList = value instanceof List;

        if (isList) {
            Type type = null;

            for (Object o : (List) value) {
                if (type == null) {
                    type = getType(o);
                } else {
                    if (type != getType(o)) {
                        throw new RuntimeException("Types are incompatible");
                    }
                }
            }

            if (type == null) {
                return Type.LIST_OF_STRINGS;
            } else {
                switch (type) {
                    case STRING:
                        return Type.LIST_OF_STRINGS;
                    case NUMBER:
                        return Type.LIST_OF_NUMBERS;
                    case BOOLEAN:
                        return Type.LIST_OF_BOOLEANS;
                    default:
                        throw new RuntimeException();
                }
            }
        } else {
            if (isString) {
                return Type.STRING;
            } else if (isNumeric) {
                return Type.NUMBER;
            } else if (isBoolean) {
                return Type.BOOLEAN;
            } else {
                throw new RuntimeException("Unknown value type");
            }
        }
    }
}
