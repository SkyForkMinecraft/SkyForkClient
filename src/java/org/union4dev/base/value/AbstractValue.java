package org.union4dev.base.value;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public abstract class AbstractValue<T> {
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private T value;
    protected ArrayList<BooleanSupplier> hideSuppliers;
    public float optionAnim;
    public float optionAnimNow;
    protected AbstractValue(String name) {
        this.name = name;
    }

    public void addSupplier(BooleanSupplier hide) {
        this.hideSuppliers.add(hide);
    }

    public boolean isHidden() {
        for(BooleanSupplier supplier : hideSuppliers){
            if(!supplier.getAsBoolean())
                return false;
        }
        return true;
    }
}
