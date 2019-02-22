package com.example.diabefriend.model;

public class ResultMeasurement {
    private Measurement measurement;
    private int sugarLevelAfterMeal;

    public ResultMeasurement(Measurement measurement, int sugarLevelAfterMeal) {
        this.measurement = measurement;
        this.sugarLevelAfterMeal = sugarLevelAfterMeal;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public int getSugarLevelAfterMeal() {
        return sugarLevelAfterMeal;
    }
}
