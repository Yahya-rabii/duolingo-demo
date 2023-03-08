package com.example.dldemo.Tests;


public class SentenceQuestion extends Question {
    private String sentence;
    private String translation;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}