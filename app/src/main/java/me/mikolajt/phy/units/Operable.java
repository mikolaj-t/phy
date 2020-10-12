package me.mikolajt.phy.units;

import java.util.LinkedHashMap;

public interface Operable {
    LinkedHashMap<BaseUnit, Integer> getBaseUnitsWithExponents();
}
