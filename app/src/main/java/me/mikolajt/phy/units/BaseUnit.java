package me.mikolajt.phy.units;

import java.util.LinkedHashMap;

public enum BaseUnit implements Unit{

    SECOND("s"),
    METRE("m"),
    KILOGRAM("kg"),
    AMPERE("A");

    private final String symbol;
    private final LinkedHashMap<BaseUnit, Integer> baseUnitsWithExponents;

    BaseUnit(String symbol){
        this.symbol = symbol;
        this.baseUnitsWithExponents = new LinkedHashMap<>();
        baseUnitsWithExponents.put(this, 1);
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public LinkedHashMap<BaseUnit, Integer> getBaseUnitsWithExponents() {
        return this.baseUnitsWithExponents;
    }
}
