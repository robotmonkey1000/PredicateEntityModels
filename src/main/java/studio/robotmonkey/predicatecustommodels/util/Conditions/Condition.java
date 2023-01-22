package studio.robotmonkey.predicatecustommodels.util.Conditions;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Condition {

    @Expose public String name;
    @Expose public String value;

    @Expose public String model;


    public boolean ConditionMet(String name, String value)
    {
        return this.name.equalsIgnoreCase(name) && this.value.equalsIgnoreCase(value);
    }

    public String GetModel()
    {
        return model;
    }
}
