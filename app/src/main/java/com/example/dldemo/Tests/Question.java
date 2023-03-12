package com.example.dldemo.Tests;

import java.io.Serializable;

public abstract class Question  implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}