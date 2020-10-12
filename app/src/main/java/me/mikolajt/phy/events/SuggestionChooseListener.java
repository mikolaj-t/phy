package me.mikolajt.phy.events;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import me.mikolajt.phy.CalculatorActivity;
import me.mikolajt.phy.units.FormulaSuggestion;

public class SuggestionChooseListener implements AdapterView.OnItemClickListener {

    private final FormulaSuggestion[] suggestions;
    private final CalculatorActivity calculatorActivity;
    private FormulaSuggestion lastSuggestion;

    public SuggestionChooseListener(FormulaSuggestion[] suggestions, CalculatorActivity activity) {
        this.suggestions = suggestions;
        this.calculatorActivity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FormulaSuggestion chosenSuggestion = suggestions[i];
        Log.i("[app]", chosenSuggestion.getUnit().getName() + " was clicked!");
        //TODO powers!!!!!!!!!
        if(lastSuggestion != null){
            calculatorActivity.replaceSuggestion(lastSuggestion, chosenSuggestion);
        }else{
            calculatorActivity.applySuggestion(chosenSuggestion);
            if(!calculatorActivity.isFullyCovered()) {
                calculatorActivity.createCalculationRow(++calculatorActivity.i);
            }else{
                //calculatorActivity.s
                //calculatorActivity.showResult();
            }
        }
        lastSuggestion = chosenSuggestion;
    }

    public FormulaSuggestion getLastSuggestion(){
        return this.lastSuggestion;
    }

}
