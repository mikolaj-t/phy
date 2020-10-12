package me.mikolajt.phy.units;

import java.util.*;

public class FormulaSuggester {
    public Set<FormulaSuggestion> suggest(Unit unit, Operable currentlyCoveredBaseUnits) {
        Operation remaining = new Operation(TypeOfOperation.MULTIPLICATION, unit, new Operation(TypeOfOperation.REVERSAL, currentlyCoveredBaseUnits));
        //System.out.println(remaining.getBaseUnitsWithExponents());
        Set<FormulaSuggestion> suggestions = new HashSet<>();
        for (int i = 1; i < Math.pow(2, remaining.getBaseUnitsWithExponents().size()); i++) {
            LinkedHashMap<BaseUnit, Integer> combination = combination(remaining.getBaseUnitsWithExponents(), i);
            //System.out.println("====================");
            // System.out.println(combination);
            //TODO not only is exact but also contains in it bc division by second^7 is stupid
            //TODO maybe like find the connecting ones
            //TODO
            if (combination.size() == 1) {
                BaseUnit bu = (BaseUnit) combination.keySet().toArray()[0];
                int val = combination.get(bu);
                if (val >= 0) {
                    suggestions.add(new FormulaSuggestion(bu, TypeOfOperation.MULTIPLICATION, val));
                } else {
                    suggestions.add(new FormulaSuggestion(bu, TypeOfOperation.REVERSAL, -val));
                }
            }else{
                //todo can add like already found list to exclude from iteration
                for (DerivedUnit du : DerivedUnit.values()) {
                    //System.out.println("combination: " + combination + " unit " + du + " ("+du.getBaseUnitsWithExponents()+")");
                    FitType fitting = fitsInCombination(du, false, combination);
                    //if(fitting.getExponent() == 0) continue;
                    //System.out.println("-----------");
                    boolean hasFoundMultiple = false;
                    switch (fitting) {
                        case NO_FIT:
                            //System.out.println("derived unit: " + du + " doesn't fit (" + du.getBaseUnitsWithExponents() + ")");
                            hasFoundMultiple = false;
                            break;
                        case FITS:
                            //System.out.println("derived unit: " + du + " fits (" + du.getBaseUnitsWithExponents() + ")");
                            suggestions.add(new FormulaSuggestion(du, TypeOfOperation.MULTIPLICATION, 1));
                            hasFoundMultiple = true;
                            break;
                        case MULTIPLE:
                            //System.out.println("derived unit: " + du + " is a multiple of " + fitting.getExponent() + " (" + du.getBaseUnitsWithExponents() + ")");
                            suggestions.add(new FormulaSuggestion(du, TypeOfOperation.MULTIPLICATION, fitting.getExponent()));
                            hasFoundMultiple = true;
                            break;
                    }
                    if(!hasFoundMultiple) {
                        fitting = fitsInCombination(du, true, combination);
                        switch (fitting) {
                            case NO_FIT:
                                //System.out.println("derived unit: " + du + " doesn't fit (" + du.getBaseUnitsWithExponents() + ")");
                                continue;
                            case FITS:
                                //System.out.println("derived unit: " + du + " fits (" + du.getBaseUnitsWithExponents() + ")");
                                suggestions.add(new FormulaSuggestion(du, TypeOfOperation.REVERSAL, 1));
                                hasFoundMultiple = true;
                                break;
                            case MULTIPLE:
                                //System.out.println("derived unit: " + du + " is a multiple of " + fitting.getExponent() + " (" + du.getBaseUnitsWithExponents() + ")");
                                suggestions.add(new FormulaSuggestion(du, TypeOfOperation.REVERSAL, fitting.getExponent()));
                                hasFoundMultiple = true;
                                break;
                        }
                    }
                    //int x = 2;
                    //System.out.println(du + "  dsfa " + fitting);
                }
            }
        }
        return suggestions;
    }

    public FitType fitsInCombination(Unit unit, boolean reverse, LinkedHashMap<BaseUnit, Integer> combination){
        if(combination.keySet().size() != unit.getBaseUnitsWithExponents().keySet().size()){
            //System.out.println("too many base units! ("+combination.keySet().size()+" " + combination + " vs "+unit.getBaseUnitsWithExponents().keySet().size()+" " + unit.getBaseUnitsWithExponents() + ")");
            return FitType.NO_FIT;
        }
        int multiplier = 0;
        boolean hasSetMutltiplier = false;
        boolean isMultiple = true;
        LinkedHashMap<BaseUnit, Integer> unitBU = unit.getBaseUnitsWithExponents();
        if(reverse){
            unitBU = reverseInts(unitBU);
        }
        for(Map.Entry<BaseUnit, Integer> entry : unitBU.entrySet()){
            if(!combination.containsKey(entry.getKey())) return FitType.NO_FIT;
            int unitExponent = entry.getValue();
            int combinationExponent = combination.get(entry.getKey());
            if(unitExponent <= combinationExponent){
                if(!hasSetMutltiplier){
                    if((combinationExponent % unitExponent) == 0 && (combinationExponent != unitExponent)) { ;
                        multiplier = combinationExponent/unitExponent;
                    }else{
                        multiplier = 1;
                    }
                    //System.out.println("multiplier " + multiplier);
                    hasSetMutltiplier = true;
                }else{
                    //System.out.println(" cE " + combinationExponent + " cE * m " + (combinationExponent * multiplier) + " uE " + unitExponent);
                    if ((combinationExponent * multiplier) != unitExponent){
                        //System.out.println("mutliple set false");
                        isMultiple = false;
                    }
                }
            }else{
                isMultiple = false;
            }
        }
        //1 2 3 - exact multipliers
        //0 - fits
        //-1 doesntFit
        if(isMultiple && multiplier != 0) {
            FitType type = FitType.MULTIPLE;
            type.setExponent(multiplier);
            //System.out.println("combination " + combination + " is a multiple of " + multiplier + " of " + unit +  "(" + unit.getBaseUnitsOperation().getBaseUnitsWithExponents() + ")");
            return type;
        }else if(!isMultiple){
            if(multiplier == 1 || multiplier == -1) {
                //System.out.println("combination " + combination + " fits " + unit + " (" + unit.getBaseUnitsOperation().getBaseUnitsWithExponents() + ")");
                return FitType.FITS;
            }else{
                //System.out.println("combination " + combination +  " doesn't fit " +  unit + " (" + unit.getBaseUnitsOperation().getBaseUnitsWithExponents() + ")");
                return FitType.NO_FIT;
            }
        }else return FitType.NO_FIT;
    }

    private enum FitType{
        NO_FIT, FITS, MULTIPLE;

        private int exponent;

        public void setExponent(int exponent) {
            this.exponent = exponent;
        }

        public int getExponent() {
            return exponent;
        }
    }

    private Map<BaseUnit, Integer> reverseInts(Map<BaseUnit, Integer> map){
        Map<BaseUnit, Integer> res = new HashMap<>();
        for(BaseUnit bu : map.keySet()){
            res.put(bu, -map.get(bu));
        }
        return res;
    }

    private LinkedHashMap<BaseUnit, Integer> reverseInts(LinkedHashMap<BaseUnit, Integer> map){
        LinkedHashMap<BaseUnit, Integer> res = new LinkedHashMap<>();
        for(BaseUnit bu : map.keySet()){
            res.put(bu, -map.get(bu));
        }
        return res;
    }

    private LinkedHashMap<BaseUnit, Integer> combination(LinkedHashMap<BaseUnit, Integer> master, int combination){
        LinkedHashMap<BaseUnit, Integer> res = new LinkedHashMap<>();
        String binary = addZeros(Integer.toBinaryString(combination), master.keySet().size());
        for(int i=0; i<binary.length(); i++){
            if(binary.charAt(i)=='1'){
                res.put((BaseUnit) master.keySet().toArray()[i], master.get(master.keySet().toArray()[i]));
            }
        }
        return res;
    }

    private String addZeros(String s, int c){
        if(s.length() < c){
            StringBuilder sb = new StringBuilder();
            for(int i=1; i<=(c-s.length()); i++){
                sb.append("0");
            }
            sb.append(s);
            return sb.toString();
        }
        return s;
    }
}
