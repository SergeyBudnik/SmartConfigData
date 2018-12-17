package com.bdev.smart.config.data.wrappers.string.parse_state;

import com.bdev.smart.config.data.wrappers.string.StringElement;

import java.util.List;

class ParseStateOnFinish extends ParseState {
    ParseStateOnFinish(
            char [] valueChars,
            int index,
            List<StringElement> elements,
            StringBuilder currentElementValue
    ) {
        super(valueChars, index, elements, currentElementValue);
    }

    @Override
    public ParseState onNextChar() {
        return null;
    }

    @Override
    public boolean isFinal() {
        return true;
    }
}
