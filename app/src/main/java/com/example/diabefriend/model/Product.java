package com.example.diabefriend.model;

import lombok.Getter;

@Getter
public class Product {

    private String name;
    private float weight;
    private float kCal;
    private float fat;
    private float carbohydrates;
    private float proteins;

    public Product(String name, float weight, float kCal, float fat, float carbohydrates, float proteins) {
        this.name = name;
        this.weight = weight;
        this.kCal = kCal;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
    }
}
