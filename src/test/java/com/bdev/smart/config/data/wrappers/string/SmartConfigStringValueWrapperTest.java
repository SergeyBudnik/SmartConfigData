package com.bdev.smart.config.data.wrappers.string;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SmartConfigStringValueWrapperTest {
    @Test
    public void test() {
        assertEquals("hello", getValue("hello"));
        assertEquals("~hi",   getValue("~hi"));
        assertEquals("~hi~~", getValue("~hi~~"));
        assertEquals("~~hi~", getValue("~~hi~"));
        assertEquals("~~hi", getValue("~~hi"));
        assertEquals(false, getValueOptional("~~env_none~~").isPresent());
        assertEquals("some_value", getValue("~~env1~~"));
        assertEquals("env2", getValue("~~env2~~"));
        assertEquals("!!~abc..?", getValue("~~env3~~"));
        assertEquals("~env1~~env2~env3~", getValue("~env1~~env2~env3~"));
        assertEquals("~env1env2hello~!!env3~", getValue("~env1~~env2~~hello~!!env3~"));
        assertEquals("~env1!!~abc..?hello~!!env3~", getValue("~env1~~env3~~hello~!!env3~"));
    }

    private String getValue(String value) {
        return getValueOptional(value).orElseThrow(RuntimeException::new);
    }

    private Optional<String> getValueOptional(String value) {
        try {
            return Optional.of(new SmartConfigStringValueWrapper(
                    value,
                    name -> {
                        switch (name) {
                            case "env1":
                                return Optional.of("some_value");
                            case "env2":
                                return Optional.of("env2");
                            case "env3":
                                return Optional.of("!!~abc..?");
                            default:
                                return Optional.empty();
                        }
                    }).getValue());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}