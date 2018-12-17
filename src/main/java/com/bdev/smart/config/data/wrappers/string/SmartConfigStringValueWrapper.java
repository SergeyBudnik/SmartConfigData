package com.bdev.smart.config.data.wrappers.string;

import com.bdev.smart.config.data.wrappers.SmartConfigValueWrapper;
import com.bdev.smart.config.data.wrappers.string.parse_state.ParseState;
import com.bdev.smart.config.data.wrappers.string.parse_state.ParseStateOnChar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;

public class SmartConfigStringValueWrapper implements SmartConfigValueWrapper<String> {
    private List<StringElement> valueParts;
    private Function<String, Optional<String>> envResolver;

    public SmartConfigStringValueWrapper(
            String value,
            Function<String, Optional<String>> envResolver
    ) {
        this.envResolver = envResolver;

        ParseState parseState = new ParseStateOnChar(
                value.toCharArray(),
                0,
                new ArrayList<>(),
                new StringBuilder()
        );

        while (!parseState.isFinal()) {
            parseState = parseState.onNextChar();
        }

        valueParts = parseState.getElements();
    }

    @Override
    public String getValue() {
        StringBuilder res = new StringBuilder();

        for (StringElement valuePart : valueParts) {
            if (valuePart.isResolvable()) {
                String value = envResolver
                        .apply(valuePart.getValue())
                        .orElseThrow(() -> new RuntimeException(format(
                            "Resolvable variable %s does not exist",
                            valuePart.getValue()
                        )));

                res.append(value);
            } else {
                res.append(valuePart.getValue());
            }
        }

        return res.toString();
    }
}
