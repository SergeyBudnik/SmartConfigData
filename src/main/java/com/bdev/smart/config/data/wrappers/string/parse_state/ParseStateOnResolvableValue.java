package com.bdev.smart.config.data.wrappers.string.parse_state;

import com.bdev.smart.config.data.wrappers.string.StringElement;

import java.util.List;

public class ParseStateOnResolvableValue extends ParseState {
    ParseStateOnResolvableValue(
            char [] valueChars,
            int index,
            List<StringElement> elements,
            StringBuilder currentElementValue
    ) {
        super(valueChars, index, elements, currentElementValue);
    }

    @Override
    public ParseState onNextChar() {
        if (index == valueChars.length) {
            elements.add(new StringElement(
                    "~~" + currentElementValue,
                    false
            ));

            return new ParseStateOnFinish(
                    valueChars, index, elements, currentElementValue
            );
        }

        char ch = valueChars[index];

        if (ch == '~') {
            return new ParseStateOnResolvableFinish(
                    valueChars, index + 1, elements, currentElementValue
            );
        } else {
            return new ParseStateOnResolvableValue(
                    valueChars, index + 1, elements, currentElementValue.append(ch)
            );
        }
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
