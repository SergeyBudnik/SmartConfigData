package com.bdev.smart.config.data.wrappers.string.parse_state;

import com.bdev.smart.config.data.wrappers.string.StringElement;

import java.util.List;

public class ParseStateOnResolvableStart extends ParseState {
    ParseStateOnResolvableStart(
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
            elements.add(new StringElement("~", false));

            return new ParseStateOnFinish(
                    valueChars, index, elements, currentElementValue
            );
        }

        char ch = valueChars[index];

        if (ch == '~') {
            return new ParseStateOnResolvableValue(
                    valueChars, index + 1, elements, new StringBuilder()
            );
        } else {
            elements.add(new StringElement("~", false));

            return new ParseStateOnChar(
                    valueChars, index + 1, elements, currentElementValue.append(ch)
            );
        }
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
