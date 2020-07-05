package me.bscal.hardcoremc.basicneeds;

public class BasicNeeds {

    public float hunger;
    public float thirst;
    public float temperature;

    public float sanity;
    public float toxicity;

    public float stamina;
    public float wellFed;

    public void UpdateNeeds(float value) {
        hunger += value;
        thirst += value;
    }

}
