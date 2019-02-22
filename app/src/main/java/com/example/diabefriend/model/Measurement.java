package com.example.diabefriend.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Measurement implements Parcelable {
    private int carbohydratesInGrams;
    private float insulinInUnits;
    private int sugarLevelBeforeMeal;

    public Measurement(int carbohydratesInGrams, float insulinInUnits, int sugarLevelBeforeMeal) {
        this.carbohydratesInGrams = carbohydratesInGrams;
        this.insulinInUnits = insulinInUnits;
        this.sugarLevelBeforeMeal = sugarLevelBeforeMeal;
    }

    public Measurement(Parcel in) {
        this.carbohydratesInGrams = in.readInt();
        this.insulinInUnits = in.readFloat();
        this.sugarLevelBeforeMeal = in.readInt();
    }

    public static final Creator<Measurement> CREATOR = new Creator<Measurement>() {
        @Override
        public Measurement createFromParcel(Parcel in) {
            return new Measurement(in);
        }

        @Override
        public Measurement[] newArray(int size) {
            return new Measurement[size];
        }
    };

    public int getCarbohydratesInGrams() {
        return carbohydratesInGrams;
    }

    public float getInsulinInUnits() {
        return insulinInUnits;
    }

    public int getSugarLevelBeforeMeal() {
        return sugarLevelBeforeMeal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(carbohydratesInGrams);
        dest.writeFloat(insulinInUnits);
        dest.writeInt(sugarLevelBeforeMeal);
    }


}
