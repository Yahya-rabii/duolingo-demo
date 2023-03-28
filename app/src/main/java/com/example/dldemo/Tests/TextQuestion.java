package com.example.dldemo.Tests;

import java.util.List;

public class TextQuestion extends Question {
    private String text;
    private String name;
    private String quote;

    private List<String> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public List<String> getOptions() {
        return options;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}

