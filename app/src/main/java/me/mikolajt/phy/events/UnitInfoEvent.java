package me.mikolajt.phy.events;

import me.mikolajt.phy.units.Unit;

public class UnitInfoEvent {
    private final Unit unit;

    public UnitInfoEvent(Unit unit){
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
}
