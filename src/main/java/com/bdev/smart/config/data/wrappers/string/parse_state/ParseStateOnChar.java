package com.bdev.smart.config.data.wrappers.string.parse_state;

import com.bdev.smart.config.data.wrappers.string.StringElement;

import java.util.List;

public class ParseStateOnChar extends ParseState {
    public ParseStateOnChar(
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
            if (currentElementValue.length() != 0) {
                elements.add(new StringElement(
                        currentElementValue.toString(),
                        false
                ));
            }

            return new ParseStateOnFinish(
                    valueChars, index, elements, currentElementValue
            );
        }

        char ch = valueChars[index];

        if (ch == '~') {
            if (currentElementValue.length() != 0) {
                elements.add(new StringElement(currentElementValue.toString(), false));
            }

            return new ParseStateOnResolvableStart(
                    valueChars, index + 1, elements, new StringBuilder()
            );
        } else {
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
