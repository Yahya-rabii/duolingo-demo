package com.example.dldemo.Tests;

import java.util.List;

public class PictureQuestion extends Question {
    private String name;
    private String image_url;
    private List<String> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_Url() {
        return image_url;
    }

    public void setImage_Url(String image_Url) {
        this.image_url = image_Url;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
