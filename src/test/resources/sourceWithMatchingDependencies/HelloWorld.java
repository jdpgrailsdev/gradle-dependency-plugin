package com.test;

import org.apache.commons.lang3.StringUtils;

public class HelloWorld {

    private final String text;

    public HelloWorld() {
        text = StringUtils.capitalize("hello world");
    }

    public String getText() {
        return text;
    }
}
