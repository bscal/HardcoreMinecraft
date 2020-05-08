package me.bscal.hardcoremc.basicneeds;

public class BasicNeeds {

    public float hunger;
    public float thirst;
    public float temperature;

    public void UpdateNeeds(float value) {
        hunger += value;
        thirst += value;
    }

    public void SetAllNeeds(float value) {
        hunger = value;
        thirst = value;
        temperature = value;
    }

    public void IncrementHunger(float amount, float multiplier) {

    }

    public void IncrementThirst(float amount, float multiplier) {

    }

    public void IncrementTemperature(float amount, float multiplier) {

    }

}
