package com.example.diabefriend.model;

public class Measurement {
    private int carbohydratesInGrams;
    private float insulinInUnits;

    public Measurement(int carbohydratesInGrams, float insulinInUnits) {
        this.carbohydratesInGrams = carbohydratesInGrams;
        this.insulinInUnits = insulinInUnits;
    }

    public int getCarbohydratesInGrams() {
        return carbohydratesInGrams;
    }

    public float getInsulinInUnits() {
        return insulinInUnits;
    }
}
