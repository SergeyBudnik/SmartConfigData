package com.bdev.smart.config.data.wrappers.string.parse_state;

import com.bdev.smart.config.data.wrappers.string.StringElement;

import java.util.List;

public abstract class ParseState {
    char [] valueChars;
    int index;
    List<StringElement> elements;
    StringBuilder currentElementValue;

    ParseState(
            char [] valueChars,
            int index,
            List<StringElement> elements,
            StringBuilder currentElementValue
    ) {
        this.valueChars = valueChars;
        this.index = index;
        this.elements = elements;
        this.currentElementValue = currentElementValue;
    }

    public List<StringElement> getElements() {
        return elements;
    }

    public abstract ParseState onNextChar();
    public abstract boolean isFinal();
}
