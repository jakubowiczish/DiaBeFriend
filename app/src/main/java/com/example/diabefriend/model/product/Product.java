package com.example.diabefriend.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

    private String name;
    private float weight;
    private float kCal;
    private float fat;
    private float carbohydrates;
    private float proteins;
}
