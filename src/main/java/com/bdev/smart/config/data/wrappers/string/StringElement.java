package com.bdev.smart.config.data.wrappers.string;

public class StringElement {
    private String value;
    private boolean isResolvable;

    public StringElement(String value, boolean isResolvable) {
        this.value = value;
        this.isResolvable = isResolvable;
    }

    public String getValue() {
        return value;
    }

    public boolean isResolvable() {
        return isResolvable;
    }
}
