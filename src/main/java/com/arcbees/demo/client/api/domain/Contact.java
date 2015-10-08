package com.arcbees.demo.client.api.domain;

public class Contact {
    private String name;
    private String email;

    public Contact() {
        // For serialization.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
