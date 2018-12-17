package com.bdev.smart.config.data;

import com.bdev.smart.config.data.SmartConfigValueTypeChecker.Type;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static com.bdev.smart.config.data.SmartConfigValueTypeChecker.Type.*;
import static org.junit.Assert.assertEquals;

public class SmartConfigValueTypeCheckerTest {
    @Test
    public void testGetType() {
        assertEquals(STRING, getType("hello"));
        assertEquals(NUMBER, getType(1));
        assertEquals(NUMBER, getType(1L));
        assertEquals(BOOLEAN, getType(false));
        assertEquals(LIST_OF_NUMBERS, getType(Arrays.asList(1, 2, 3)));
        assertEquals(LIST_OF_STRINGS, getType(Arrays.asList("a", "b", "c")));
        assertEquals(LIST_OF_BOOLEANS, getType(Arrays.asList(true, false)));
        assertEquals(false, getTypeOptional(Arrays.asList(true, "a", 3L)).isPresent());
    }

    private <T> Type getType(T value) {
        return getTypeOptional(value).orElseThrow(RuntimeException::new);
    }

    private <T> Optional<Type> getTypeOptional(T value) {
        try {
            return Optional.of(SmartConfigValueTypeChecker.getType(value));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}