package me.mikolajt.phy.units;

import java.util.LinkedHashMap;
import java.util.Map;

public class Operation implements Operable{
    private final TypeOfOperation type;
    /*private Operation operation1;
    private Operation operation2;*/
    private Operable[] operables;
    private short mode;
    private LinkedHashMap<BaseUnit, Integer> baseUnitsWithExponents;

   /* public Operation(BaseUnit unit){
        this.type = TypeOfOperation.SINGLE;
        this.baseUnitsWithExponents = new LinkedHashMap<>();
        this.baseUnitsWithExponents.put(unit, 1);
        this.mode = -1;
        this.operation1 = this;
    }

    public Operation(TypeOfOperation type, BaseUnit unit1, BaseUnit unit2) {
        this.type = type;
        this.operation1 = unit1.getBaseUnitsOperation();
        this.operation2 = unit2.getBaseUnitsOperation();
        this.mode = 3;
        this.baseUnitsWithExponents = getBaseUnitsWithPowers();
    }

    public Operation(TypeOfOperation type, BaseUnit unit1, Operation operation2) {
        this.type = type;
        this.operation1 = unit1.getBaseUnitsOperation();
        this.operation2 = operation2;
        //this.mode = 1;
        this.mode = 3;
        this.baseUnitsWithExponents = getBaseUnitsWithPowers();
    }

    public Operation(TypeOfOperation type, Operation operation1, BaseUnit unit2) {
        this.type = type;
        this.operation1 = operation1;
        this.operation2 = unit2.getBaseUnitsOperation();
        //this.mode = 2;
        this.mode = 3;
        this.baseUnitsWithExponents = getBaseUnitsWithPowers();
    }

    public Operation(TypeOfOperation type, Operation operation1, Operation operation2) {
        this.type = type;
        this.operation1 = operation1;
        this.operation2 = operation2;
        this.mode = 3;
        this.baseUnitsWithExponents = getBaseUnitsWithPowers();
    }*/

    public Operation(TypeOfOperation type, Operable... operables){
        this.type = type;
        this.operables = operables;
        this.baseUnitsWithExponents = calculateBaseUnitsWithPowers();
    }

    public LinkedHashMap<BaseUnit, Integer> getBaseUnitsWithExponents() {
        return baseUnitsWithExponents;
    }

    private LinkedHashMap<BaseUnit, Integer> calculateBaseUnitsWithPowers() {
        LinkedHashMap<BaseUnit, Integer> m = new LinkedHashMap<>();
        /*if(mode == -1){
            for(Map.Entry<BaseUnit, Integer> entry : operation1.getBaseUnitsWithExponents().entrySet()){
                m.put(entry.getKey(), entry.getValue());
            }
        }else if(mode==3) {
            if (operation2 == null && operation1 != null) return operation1.getBaseUnitsWithPowers();
            if (operation1 == null && operation2 != null) return operation2.getBaseUnitsWithPowers();
            for (Map.Entry<BaseUnit, Integer> entry : operation1.getBaseUnitsWithPowers().entrySet()) {
                alterValuesInMap(entry.getKey(), entry.getValue(), m);
            }
            for (Map.Entry<BaseUnit, Integer> entry : operation2.getBaseUnitsWithPowers().entrySet()) {
                if (type == TypeOfOperation.MULTIPLICATION) {
                    alterValuesInMap(entry.getKey(), entry.getValue(), m);
                } else if (type == TypeOfOperation.DIVISION) {
                    alterValuesInMap(entry.getKey(), -entry.getValue(), m);
                }
            }
        }*/
        for(Operable operable : this.operables){
            try {
                //System.out.println(this.type + " " + operable.getBaseUnitsWithExponents());
                int i = 0;
                for (Map.Entry<BaseUnit, Integer> entry : operable.getBaseUnitsWithExponents().entrySet()) {
                    if (type == TypeOfOperation.MULTIPLICATION) {
                        alterValuesInMap(entry.getKey(), entry.getValue(), m);
                    } else if (type == TypeOfOperation.REVERSAL) {
                        alterValuesInMap(entry.getKey(), -entry.getValue(), m);
                    }
                    i++;
                }
            }catch(NullPointerException e){
                continue;
            }
        }
        return m;
    }

    public void alterValuesInMap(BaseUnit unit, int amount, Map<BaseUnit,Integer> map){
        if(amount == 0) return;
        if(map.containsKey(unit)){
            int res = map.get(unit) + amount;
            if(res != 0) {
                map.put(unit, res);
            }else{
                map.remove(unit);
            }
        }else{
            map.put(unit, amount);
        }
    }

    public TypeOfOperation getType() {
        return type;
    }

    /*public Operation getOperation1() {
        return operation1;
    }

    public Operation getOperation2() {
        return operation2;
    }*/
}
