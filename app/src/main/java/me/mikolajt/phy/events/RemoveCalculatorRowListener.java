package me.mikolajt.phy.events;

import android.view.View;
import android.view.ViewGroup;
import me.mikolajt.phy.CalculatorActivity;
import me.mikolajt.phy.units.FormulaSuggestion;
import me.mikolajt.phy.units.Unit;

public class RemoveCalculatorRowListener implements View.OnClickListener{

    private final FormulaSuggestion suggestion;
    private final CalculatorActivity calculatorActivity;
    private final View calculatorRow;

    public RemoveCalculatorRowListener(FormulaSuggestion suggestion, CalculatorActivity calculatorActivity, View calculatorRow) {
        this.suggestion = suggestion;
        this.calculatorActivity = calculatorActivity;
        this.calculatorRow = calculatorRow;
    }

    @Override
    public void onClick(View view) {
        calculatorActivity.removeSuggestion(suggestion);
        ((ViewGroup)calculatorRow.getParent()).removeView(calculatorRow);
    }
}
