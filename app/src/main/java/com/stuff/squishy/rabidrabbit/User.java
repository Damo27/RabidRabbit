package com.stuff.squishy.rabidrabbit;

//------------------------------Object class to save user information------------------------------

public class User
{
    private String name;
    private String weight;
    private String height;
    private int stepGoal;
    private int weightGoal;
    private boolean isImperial = false;

    public boolean isImperial()
    {
        return isImperial;
    }

    public void setImperial(boolean imperial)
    {
        isImperial = imperial;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public int getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(int weightGoal) {
        this.weightGoal = weightGoal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
