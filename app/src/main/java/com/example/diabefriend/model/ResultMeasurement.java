package com.example.diabefriend.model;

public class ResultMeasurement {
    private Measurement measurement;
    private int sugarLevelAfterMeal;

    public ResultMeasurement(Measurement measurement, int sugarLevelAfterMeal) {
        this.measurement = measurement;
        this.sugarLevelAfterMeal = sugarLevelAfterMeal;
    }
}
