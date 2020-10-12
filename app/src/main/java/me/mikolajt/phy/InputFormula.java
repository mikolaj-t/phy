package me.mikolajt.phy;

import me.mikolajt.phy.units.FormulaSuggestion;

public class InputFormula extends FormulaSuggestion {

    private int inputValue;

    public InputFormula(FormulaSuggestion suggestion) {
        super(suggestion.getUnit(), suggestion.getTypeOfOperation(), suggestion.getExponent());
    }

    public int getInputValue() {
        return inputValue;
    }

    public void setInputValue(int inputValue) {
        this.inputValue = inputValue;
    }
}
