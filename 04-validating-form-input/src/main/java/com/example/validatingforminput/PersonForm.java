package com.example.validatingforminput;

import jakarta.validation.constraints.*;

public class PersonForm {

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Min(18)
    private Integer age;

    @NotBlank
    @Email
    private String email;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Person(Name: " + this.name + ", Age: " + this.age + ", Email: " + this.email + ")";
    }
}