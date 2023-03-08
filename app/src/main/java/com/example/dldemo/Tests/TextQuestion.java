package com.example.dldemo.Tests;

import java.util.List;

public class TextQuestion extends Question {
    private String text;
    private List<String> errors;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

