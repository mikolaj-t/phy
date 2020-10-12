package me.mikolajt.phy.units;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormulaSuggestion implements Operable{
    private final Unit unit;
    private final TypeOfOperation typeOfOperation;
    private final int exponent;
    private final LinkedHashMap<BaseUnit, Integer> baseUnitsWithExponents;

    public FormulaSuggestion(Unit unit, TypeOfOperation typeOfOperation, int exponent) {
        this.unit = unit;
        this.typeOfOperation = typeOfOperation;
        this.exponent = exponent;
        this.baseUnitsWithExponents = calculateBaseUnitsWithExponents();
    }

    public Unit getUnit() {
        return unit;
    }

    public TypeOfOperation getTypeOfOperation() {
        return typeOfOperation;
    }

    public int getExponent() {
        return exponent;
    }

    private LinkedHashMap<BaseUnit, Integer> calculateBaseUnitsWithExponents(){
        LinkedHashMap<BaseUnit, Integer> res = new LinkedHashMap<>();
        for(Map.Entry<BaseUnit, Integer> entry : unit.getBaseUnitsWithExponents().entrySet()){
            res.put(entry.getKey(), entry.getValue() * exponent);
        }
        return res;
    }

    public LinkedHashMap<BaseUnit, Integer> getBaseUnitsWithExponents(){
        return this.baseUnitsWithExponents;
    }

    @Override
    public String toString() {
        /*return "FormulaSuggestion{" +
                "unit=" + unit.getName() +
                ", typeOfOperation=" + typeOfOperation +
                ", exponent=" + exponent +
                '}';*/
        StringBuilder sb = new StringBuilder();
        if(typeOfOperation == TypeOfOperation.MULTIPLICATION){
            sb.append("*");
        }else if(typeOfOperation == TypeOfOperation.REVERSAL){
            sb.append("\\");
        }
        sb.append(unit.getSymbol());
        sb.append("^");
        sb.append(exponent);
        return sb.toString();
    }

   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormulaSuggestion that = (FormulaSuggestion) o;

        if (exponent != that.exponent) return false;
        if (!unit.equals(that.unit)) return false;
        return typeOfOperation == that.typeOfOperation;
    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + typeOfOperation.hashCode();
        result = 31 * result + exponent;
        return result;
    }
}
