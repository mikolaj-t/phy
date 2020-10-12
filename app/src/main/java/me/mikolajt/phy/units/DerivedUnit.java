package me.mikolajt.phy.units;

import java.util.LinkedHashMap;

public enum DerivedUnit implements Unit{

    METRE_PER_SECOND("m/s", new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.METRE, new Operation(TypeOfOperation.REVERSAL, BaseUnit.SECOND))),
    NEWTON("N", new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.KILOGRAM, new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.METRE, new Operation(TypeOfOperation.REVERSAL, new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.SECOND, BaseUnit.SECOND))))),
    PASCAL("Pa", new Operation(TypeOfOperation.MULTIPLICATION, NEWTON, new Operation(TypeOfOperation.REVERSAL, new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.METRE, BaseUnit.METRE)))),
    KILOGRAM_TIMES_METRE_PER_SECOND("kg*m/s", new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.KILOGRAM, METRE_PER_SECOND)),
    JOULE("J", new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.METRE, NEWTON)),
    WATT("W", new Operation(TypeOfOperation.MULTIPLICATION, JOULE, new Operation(TypeOfOperation.REVERSAL, BaseUnit.SECOND))),
    COULOMB("C", new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.AMPERE, BaseUnit.SECOND)),
    VOLT("V", new Operation(TypeOfOperation.MULTIPLICATION, WATT, new Operation(TypeOfOperation.REVERSAL, BaseUnit.AMPERE))),
    FARAD("F", new Operation(TypeOfOperation.MULTIPLICATION, COULOMB, new Operation(TypeOfOperation.REVERSAL, VOLT))),
    OHM("Ω", new Operation(TypeOfOperation.MULTIPLICATION, VOLT, new Operation(TypeOfOperation.REVERSAL, BaseUnit.AMPERE)))
    ;
    private final String symbol;
    private final Operation operationsOnBaseUnits;

    DerivedUnit(String symbol, Operation operationsOnBaseUnits) {
        this.symbol = symbol;
        this.operationsOnBaseUnits = operationsOnBaseUnits;
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
        return this.operationsOnBaseUnits.getBaseUnitsWithExponents();
    }
}
