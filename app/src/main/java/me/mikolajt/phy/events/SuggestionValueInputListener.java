package me.mikolajt.phy.events;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import me.mikolajt.phy.CalculatorActivity;
import me.mikolajt.phy.units.FormulaSuggestion;
import me.mikolajt.phy.units.TypeOfOperation;

public class SuggestionValueInputListener implements TextWatcher {

    private final TextInputEditText inputText;
    private final CalculatorActivity calculatorActivity;
    private final SuggestionChooseListener suggestionChooseListener;


    public SuggestionValueInputListener(TextInputEditText inputText, CalculatorActivity calculatorActivity, SuggestionChooseListener suggestionChooseListener) {
        this.inputText = inputText;
        this.calculatorActivity = calculatorActivity;
        this.suggestionChooseListener = suggestionChooseListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        FormulaSuggestion lastChosen = suggestionChooseListener.getLastSuggestion();
        if(lastChosen != null) {
            float inputValue = 0;
            if(!inputText.getText().toString().isEmpty())
            {
                inputValue = Float.parseFloat(inputText.getText().toString());
            }
            if(!(inputValue==0 && lastChosen.getTypeOfOperation() == TypeOfOperation.REVERSAL)){
                calculatorActivity.addFormulaValue(lastChosen, inputValue);
                if(calculatorActivity.isFullyCovered()){
                    calculatorActivity.showResult();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
